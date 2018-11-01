package helpers

// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

import java.util.ArrayList
import java.lang.StringBuilder
import java.io.BufferedWriter
import java.io.FileWriter
import java.nio.file.Paths
import java.nio.file.Files

// Represents a node in a JSON DOM. Here is an example usage of this class:
//
// class Dohickey
// {
//     boolean b;
//     int i;
//     double d;
//     Thingy t;
//     ArrayList<Thingy> l;
//
//
//     // Unmarshaling constructor
//     Dohickey(Json ob)
//     {
//         b = ob.boolean("b");
//         i = (int)ob.long("i");
//         d = ob.double("d");
//         t = new Thingy(ob.get("t"));
//         l = new ArrayList<Thingy>();
//         Json ll = ob.get("l");
//         for(int j = 0; j < ll.size(); j++)
//             l.add(new Thingy(ll.get(j)));
//     }
//
//
//     // Marshals this object into a JSON DOM
//     Json marshal()
//     {
//         Json ob = Json.newObject();
//         ob.add("b", b);
//         ob.add("i", i);
//         ob.add("d", d);
//         ob.add("t", t.marshal());
//         Json ll = Json.newList();
//         ob.add(ll);
//         for(int i = 0; i < l.size(); i++)
//             ll.add(l.get(i).marshal());
//     }
// }
abstract class Json {
    abstract fun write(sb: StringBuilder)

    open val size get() = asList().size

    open operator fun get(name: String) = asObject().field(name)

    open operator fun get(index: Int) = asList()[index]

    open fun boolean(name: String) = get(name).asBool()

    open fun boolean(index: Int) = get(index).asBool()

    open fun long(name: String) = get(name).asLong()

    open fun long(index: Int) = get(index).asLong()

    open fun double(name: String) = get(name).asDouble()

    open fun double(index: Int) = get(index).asDouble()

    open fun string(name: String) = get(name).asString()

    open fun string(index: Int) = get(index).asString()

    open fun add(name: String, v: Json) = asObject().add(name, v)

    open fun add(name: String, v: Boolean) = asObject().add(name, JBool(v))

    open fun add(name: String, v: Long) = asObject().add(name, JLong(v))

    open fun add(name: String, v: Double) = asObject().add(name, JDouble(v))

    open fun add(name: String, v: String) = asObject().add(name, JString(v))

    open fun add(item: Json): Boolean = asList().add(item)

    open fun add(v: Boolean) = asList().add(JBool(v))

    open fun add(v: Long) = asList().add(JLong(v))

    open fun add(v: Double) = asList().add(JDouble(v))

    open fun add(v: String) = asList().add(JString(v))

    open fun asBool() = (this as JBool).value

    open fun asLong(): Long = (this as JLong).value

    open fun asDouble(): Double = (this as JDouble).value

    open fun asString(): String = (this as JString).value

    override fun toString() = StringBuilder().apply { write(this) }.toString()

    private fun asObject() = this as JObject

    private fun asList() = this as JList

    fun save(filename: String) {
        try {
            BufferedWriter(FileWriter(filename)).apply {
                write(toString())
                close()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    class StringParser constructor(var str: String) {
        var pos = 0

        val remaining get() = str.length - pos

        fun peek() = str[pos]

        fun advance(n: Int) {
            pos += n
        }

        fun skipWhitespace() {
            while (pos < str.length && str[pos] <= ' ')
                pos++
        }

        fun expect(s: String) {
            if (str.substring(pos, Math.min(str.length, pos + s.length)) != s)
                throw RuntimeException(
                    "Expected \"" + s + "\", Got \"" + str.substring(pos, Math.min(str.length, pos + s.length)) + "\"")
            pos += s.length
        }

        fun until(c: Char): String {
            var i = pos
            while (i < str.length && str[i] != c)
                i++
            val s = str.substring(pos, i)
            pos = i
            return s
        }

        fun until(a: Char, b: Char): String {
            var i = pos
            while (i < str.length && str[i] != a && str[i] != b)
                i++
            val s = str.substring(pos, i)
            pos = i
            return s
        }

        fun untilWhitespace(): String {
            var i = pos
            while (i < str.length && str[i] > ' ')
                i++
            val s = str.substring(pos, i)
            pos = i
            return s
        }

        fun untilQuoteSensitive(a: Char, b: Char): String {
            return if (peek() == '"') {
                advance(1)
                val s = "\"" + until('"') + "\""
                advance(1)
                until(a, b)
                s
            } else
                until(a, b)
        }

        fun whileReal(): String {
            var i = pos
            while (i < str.length) {
                val c = str[i]
                if (c in '0'..'9' ||
                    c == '-' ||
                    c == '+' ||
                    c == '.' ||
                    c == 'e' ||
                    c == 'E')
                    i++
                else
                    break
            }
            val s = str.substring(pos, i)
            pos = i
            return s
        }
    }

    private class NameVal constructor(var name: String?, v: Json?) {
        var value = when {
            name == null -> throw IllegalArgumentException("The name cannot be null")
            v == null -> JNull()
            else -> v
        }
    }

    private class JObject : Json() {
        var fields: ArrayList<NameVal> = ArrayList()

        override fun add(name: String, v: Json) {
            fields.add(NameVal(name, v))
        }

        fun fieldIfExists(name: String): Json? {
            for (nv in fields)
                if (nv.name == name)
                    return nv.value
            return null
        }

        fun field(name: String): Json {
            return fieldIfExists(name) ?: throw RuntimeException("No field named \"$name\" found.")
        }

        override fun write(sb: StringBuilder) {
            sb.append("{")
            for (i in fields.indices) {
                if (i > 0)
                    sb.append(",")
                val nv = fields[i]
                JString.write(sb, nv.name!!)
                sb.append(":")
                nv.value.write(sb)
            }
            sb.append("}")
        }

        companion object {

            fun parseObject(p: StringParser): JObject {
                p.expect("{")
                val newOb = JObject()
                var readyForField = true
                while (p.remaining > 0) {
                    val c = p.peek()
                    when {
                        c <= ' ' -> p.advance(1)
                        c == '}' -> {
                            p.advance(1)
                            return newOb
                        }
                        c == ',' -> {
                            if (readyForField)
                                throw RuntimeException("Unexpected ','")
                            p.advance(1)
                            readyForField = true
                        }
                        c == '\"' -> {
                            if (!readyForField)
                                throw RuntimeException("Expected a ',' before the next field in JSON file")
                            p.skipWhitespace()
                            val name = JString.parseString(p)
                            p.skipWhitespace()
                            p.expect(":")
                            val value = parseNode(p)
                            newOb.add(name, value)
                            readyForField = false
                        }
                        else -> throw RuntimeException("Expected a '}' or a '\"'. Got " + p.str.substring(p.pos, p.pos + 10))
                    }
                }
                throw RuntimeException("Expected a matching '}' in JSON file")
            }
        }
    }

    private class JList : Json() {
        var list: ArrayList<Json> = ArrayList()

        fun add(item: Json?) {
            list.add(item ?: JNull())
        }

        override val size get() = list.size

        override fun get(index: Int) = list[index]

        override fun write(sb: StringBuilder) {
            sb.append("[")
            for (i in list.indices) {
                if (i > 0)
                    sb.append(",")
                list[i].write(sb)
            }
            sb.append("]")
        }

        companion object {
            fun parseList(p: StringParser): JList {
                p.expect("[")
                val newList = JList()
                var readyForValue = true
                while (p.remaining > 0) {
                    p.skipWhitespace()
                    readyForValue = when (p.peek()) {
                        ']' -> {
                            p.advance(1)
                            return newList
                        }
                        ',' -> {
                            if (readyForValue)
                                throw RuntimeException("Unexpected ',' in JSON file")
                            p.advance(1)
                            true
                        }
                        else -> {
                            if (!readyForValue)
                                throw RuntimeException("Expected a ',' or ']' in JSON file")
                            newList.list.add(parseNode(p))
                            false
                        }
                    }
                }
                throw RuntimeException("Expected a matching ']' in JSON file")
            }
        }
    }

    private class JBool constructor(var value: Boolean) : Json() {

        override fun write(sb: StringBuilder) {
            sb.append(if (value) "true" else "false")
        }
    }

    private class JLong constructor(var value: Long) : Json() {

        override fun write(sb: StringBuilder) {
            sb.append(value)
        }
    }

    private class JDouble constructor(var value: Double) : Json() {

        override fun write(sb: StringBuilder) {
            sb.append(value)
        }

        companion object {

            fun parseNumber(p: StringParser): Json {
                val s = p.whileReal()
                return if (s.indexOf('.') >= 0)
                    JDouble(java.lang.Double.parseDouble(s))
                else
                    JLong(java.lang.Long.parseLong(s))
            }
        }
    }

    private class JString constructor(var value: String) : Json() {

        override fun write(sb: StringBuilder) {
            write(sb, value)
        }

        companion object {

            fun write(sb: StringBuilder, value: String) {
                sb.append('"')
                for (i in 0 until value.length) {
                    val c = value[i]
                    when {
                        c < ' ' -> when (c) {
                            '\b' -> sb.append("\\b")
                            '\n' -> sb.append("\\n")
                            '\r' -> sb.append("\\r")
                            '\t' -> sb.append("\\t")
                            else -> sb.append(c)
                        }
                        c == '\\' -> sb.append("\\\\")
                        c == '"' -> sb.append("\\\"")
                        else -> sb.append(c)
                    }
                }
                sb.append('"')
            }

            fun parseString(p: StringParser): String {
                val sb = StringBuilder()
                p.expect("\"")
                while (p.remaining > 0) {
                    var c = p.peek()
                    when (p.peek()) {
                        '\"' -> {
                            p.advance(1)
                            return sb.toString()
                        }
                        '\\' -> {
                            p.advance(1)
                            c = p.peek()
                            p.advance(1)
                            when (c) {
                                '"' -> sb.append('"')
                                '\\' -> sb.append('\\')
                                '/' -> sb.append('/')
                                'b' -> sb.append('\b')
                                'n' -> sb.append('\n')
                                'r' -> sb.append('\r')
                                't' -> sb.append('\t')
                                'u' -> throw RuntimeException("Sorry, unicode characters are not yet supported")
                                else -> throw RuntimeException("Unrecognized escape sequence")
                            }
                        }
                        else -> {
                            sb.append(c)
                            p.advance(1)
                        }
                    }
                }
                throw RuntimeException("No closing \"")
            }
        }
    }

    private class JNull : Json() {

        override fun write(sb: StringBuilder) {
            sb.append("null")
        }
    }

    companion object {
        fun newObject(): Json = JObject()

        fun newList(): Json = JList()

        fun parseNode(p: StringParser): Json {
            p.skipWhitespace()
            if (p.remaining == 0)
                throw RuntimeException("Unexpected end of JSON file")
            val c = p.peek()
            when (c) {
                '"' -> return JString(JString.parseString(p))
                '{' -> return JObject.parseObject(p)
                '[' -> return JList.parseList(p)
                't' -> {
                    p.expect("true")
                    return JBool(true)
                }
                'f' -> {
                    p.expect("false")
                    return JBool(false)
                }
                'n' -> {
                    p.expect("null")
                    return JNull()
                }
                else -> return if (c in '0'..'9' || c == '-')
                    JDouble.parseNumber(p)
                else
                    throw RuntimeException("Unexpected token at " + p.str.substring(p.pos, Math.min(p.remaining, 50)))
            }
        }

        fun parse(s: String): Json = parseNode(StringParser(s))

        fun load(filename: String): Json {
            val contents: String
            try {
                contents = String(Files.readAllBytes(Paths.get(filename)))
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

            return parse(contents)
        }
    }
}