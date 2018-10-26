package model

// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

import helpers.Vec.marshal
import helpers.Vec.vecString
import helpers.Json
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.io.PrintStream
import java.lang.Double.MAX_VALUE
import java.lang.Math.max
import java.lang.Math.min
import java.util.HashMap
import java.util.Scanner
import java.util.ArrayList
import java.lang.StringBuilder
import java.util.Comparator

// This stores a matrix, A.K.A. data set, A.K.A. table. Each element is
// represented as a double value. Nominal values are represented using their
// corresponding zero-indexed enumeration value. For convenience,
// the matrix also stores some meta-data which describes the columns (or attributes)
// in the matrix.
class Matrix {
    // Data
    private val data = ArrayList<DoubleArray>() // matrix elements

    // Meta-data
    private var filename = ""
    private var attrNames = ArrayList<String>()
    private var strsToEnums = ArrayList<HashMap<String, Int>>() // value to enumeration
    private var enumsToStrs = ArrayList<HashMap<Int, String>>() // enumeration to value

    fun marshal() = Json.newList().also {
        repeat(rows) { r ->
            it.add(row(r).marshal())
        }
    }

    // Loads the matrix from an ARFF file
    fun loadARFF(filename: String) {
        var attrCount = 0 // Count number of attributes
        var lineNum = 0 // Used for exception messages

        strsToEnums.clear()
        enumsToStrs.clear()
        attrNames.clear()
        try {
            Scanner(File(filename)).use { s ->
                while (s.hasNextLine()) {
                    lineNum++
                    var line = s.nextLine().trim { it <= ' ' }
                    val upper = line.toUpperCase()

                    if (upper.startsWith("@RELATION"))
                        this.filename = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    else if (upper.startsWith("@ATTRIBUTE")) {
                        val strToEnum = HashMap<String, Int>()
                        val enumToStr = HashMap<Int, String>()
                        strsToEnums.add(strToEnum)
                        enumsToStrs.add(enumToStr)

                        val sp = Json.StringParser(line)
                        sp.advance(10)
                        sp.skipWhitespace()
                        val attrName = sp.untilWhitespace()
                        attrNames.add(attrName)
                        sp.skipWhitespace()
                        var valCount = 0
                        if (sp.peek() == '{') {
                            sp.advance(1)
                            while (sp.peek() != '}') {
                                sp.skipWhitespace()
                                val attrVal = sp.untilQuoteSensitive(',', '}')
                                if (sp.peek() == ',')
                                    sp.advance(1)
                                if (strToEnum.containsKey(attrVal))
                                    throw RuntimeException("Duplicate attribute value: $attrVal")
                                strToEnum[attrVal] = valCount
                                enumToStr[valCount] = attrVal
                                valCount++
                            }
                            sp.advance(1)
                        }
                        attrCount++
                    } else if (upper.startsWith("@DATA")) {
                        data.clear()

                        while (s.hasNextLine()) {
                            lineNum++
                            line = s.nextLine().trim { it <= ' ' }
                            if (line.startsWith("%") || line.isEmpty())
                                continue
                            val row = DoubleArray(attrCount)
                            data.add(row)
                            val sp = Json.StringParser(line)
                            for (i in 0 until attrCount) {
                                sp.skipWhitespace()
                                val v = sp.untilQuoteSensitive(',', '\n')

                                val valueCount = enumsToStrs[i].size
                                when {
                                    v == "?"
                                        // Unknown values are always set to UNKNOWN_VALUE
                                    -> row[i] = UNKNOWN_VALUE
                                    valueCount > 0
                                        // if it's nominal
                                    -> {
                                        val enumMap = strsToEnums[i]
                                        if (!enumMap.containsKey(v)) {
                                            throw IllegalArgumentException(
                                                "Unrecognized enumeration value $v on line: $lineNum")
                                        }

                                        row[i] = enumMap[v]!!.toDouble()
                                    }
                                    else
                                        // else it's continuous
                                    -> row[i] = java.lang.Double.parseDouble(v)
                                } // The attribute is continuous

                                sp.advance(1)
                            }
                        }
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            throw IllegalArgumentException("Failed to open file: $filename")
        }
    }

    override fun toString() = StringBuilder().apply {
        repeat(rows) { r ->
            if (r > 0) append("\n")
            append(row(r).vecString)
        }
    }.toString()

    private fun rowString(row: DoubleArray): String {
        val sb = StringBuilder()
        if (row.size != cols)
            throw RuntimeException("Unexpected row size")
        for (j in row.indices) {
            if (row[j] == UNKNOWN_VALUE)
                sb.append("?")
            else {
                val vals = valueCount(j)
                if (vals == 0) {
                    if (Math.floor(row[j]) == row[j])
                        sb.append(Math.floor(row[j]).toInt())
                    else
                        sb.append(row[j])
                } else {
                    val v = row[j].toInt()
                    if (v >= vals)
                        throw IllegalArgumentException("Value out of range.")
                    sb.append(attrValue(j, v))
                }
            }

            if (j + 1 < cols)
                sb.append(",")
        }

        return sb.toString()
    }

    fun printRow(row: DoubleArray, os: PrintStream) = os.print(rowString(row))

    fun printRow(row: DoubleArray, os: PrintWriter) = os.print(rowString(row))

    // Saves the matrix to an ARFF file
    fun saveARFF(filename: String) {
        try {
            PrintWriter(filename).use { os ->
                // Print the relation name, if one has been provided ('x' is default)
                os.print("@RELATION ")
                os.println(if (filename.isEmpty()) "x" else filename)

                // Print each attribute in order
                for (i in attrNames.indices) {
                    os.print("@ATTRIBUTE ")

                    val attributeName = attrNames[i]
                    os.print(if (attributeName.isEmpty()) "x" else attributeName)

                    val vals = valueCount(i)

                    if (vals == 0)
                        os.println(" REAL")
                    else {
                        os.print(" {")
                        for (j in 0 until vals) {
                            os.print(attrValue(i, j))
                            if (j + 1 < vals) os.print(",")
                        }
                        os.println("}")
                    }
                }

                // Print the data
                os.println("@DATA")
                for (i in 0 until rows) {
                    val row = data[i]
                    printRow(row, os)
                    os.println()
                }
            }
        } catch (e: FileNotFoundException) {
            throw IllegalArgumentException("Error creating file: $filename")
        }
    }

    // Makes a rows-by-columns matrix of *ALL CONTINUOUS VALUES*.
    // This method wipes out any data currently in the matrix. It also
    // wipes out any meta-data.
    fun setSize(rows: Int, cols: Int) {
        data.clear()

        // Set the meta-data
        filename = ""
        attrNames.clear()
        strsToEnums.clear()
        enumsToStrs.clear()

        // Make space for each of the columns, then each of the rows
        newColumns(cols)
        newRows(rows)
    }

    // Clears this matrix and copies the meta-data from that matrix.
    // In other words, it makes a zero-row matrix with the same number
    // of columns as "that" matrix. You will need to call newRow or newRows
    // to give the matrix some rows.
    fun copyMetaData(that: Matrix) {
        data.clear()
        attrNames = ArrayList(that.attrNames)

        // Make a deep copy of that.strsToEnums
        strsToEnums = ArrayList()
        for (map in that.strsToEnums) {
            val temp = HashMap<String, Int>()
            for ((key, value) in map)
                temp[key] = value

            strsToEnums.add(temp)
        }

        // Make a deep copy of that.m_enum_to_string
        enumsToStrs = ArrayList()
        for (map in that.enumsToStrs) {
            val temp = HashMap<Int, String>()
            for ((key, value) in map)
                temp[key] = value

            enumsToStrs.add(temp)
        }
    }

    // Adds a column with the specified name
    fun newColumn(name: String) {
        data.clear()
        attrNames.add(name)
        strsToEnums.add(HashMap())
        enumsToStrs.add(HashMap())
    }

    // Adds a column to this matrix with the specified number of values. (Use 0 for
    // a continuous attribute.) This method also sets the number of rows to 0, so
    // you will need to call newRow or newRows when you are done adding columns.
    fun newColumn(vals: Int) {
        data.clear()
        val name = "col_$cols"

        attrNames.add(name)

        val tmpStrToEnum = HashMap<String, Int>()
        val tmpEnumToStr = HashMap<Int, String>()

        repeat(vals) {
            val sVal = "val_$it"
            tmpStrToEnum[sVal] = it
            tmpEnumToStr[it] = sVal
        }

        strsToEnums.add(tmpStrToEnum)
        enumsToStrs.add(tmpEnumToStr)
    }

    // Adds a column to this matrix with 0 values (continuous data).
    fun newColumn() = newColumn(0)

    // Adds n columns to this matrix, each with 0 values (continuous data).
    fun newColumns(n: Int) = repeat(n) { newColumn() }

    // Returns the index of the specified value in the specified column.
    // If there is no such value, adds it to the column.
    fun findOrCreateValue(column: Int, v: String): Int {
        val i = strsToEnums[column][v]
        return if (i == null) {
            val nextVal = enumsToStrs[column].size
            enumsToStrs[column][nextVal] = v
            strsToEnums[column][v] = nextVal
            nextVal
        } else i
    }

    // Adds one new row to this matrix. Returns a reference to the new row.
    fun newRow() {
        if (cols == 0) throw noColumnsException
        data.add(DoubleArray(cols))
    }

    // Adds one new row to this matrix at the specified location. Returns a reference to the new row.
    fun insertRow(i: Int): DoubleArray {
        if (cols == 0) throw noColumnsException
        return DoubleArray(cols).also { data.add(i, it) }
    }

    // Removes the specified row from this matrix. Returns a reference to the removed row.
    fun removeRow(i: Int): DoubleArray {
        return data.removeAt(i)
    }

    // Appends the specified row to this matrix.
    fun takeRow(row: DoubleArray) {
        if (row.size != cols) throw incompatibleRowColSizeException
        data.add(row)
    }

    // Adds 'n' new rows to this matrix
    fun newRows(n: Int) = repeat(n) { newRow() }

    // Returns the number of rows in the matrix
    val rows get() = data.size

    // Returns the number of columns (or attributes) in the matrix
    val cols get() = attrNames.size

    // Returns the name of the specified attribute
    fun attrName(col: Int) = attrNames[col]

    // Returns the name of the specified value
    fun attrValue(attr: Int, value: Int) = enumsToStrs[attr][value] ?: throw IllegalArgumentException("No name")

    fun getString(r: Int, c: Int) = attrValue(c, row(r)[c].toInt())

    // Returns the enumerated index of the specified string
    fun valueEnum(attr: Int, v: String): Int {
        val i = strsToEnums[attr][v]
        if (i == null) {
            // Make a very detailed error message listing all possible choices
            val s = StringBuilder()
            for ((key, value) in strsToEnums[attr]) {
                if (s.isNotEmpty())
                    s.append(", ")
                s.append("\"").append(key).append("\"")
                s.append("->")
                s.append(Integer.toString(value))
            }
            throw IllegalArgumentException("No such value: \"$v\". Choices are: $s")
        } else
            return i
    }

    // Returns a reference to the specified row
    fun row(index: Int) = data[index]

    // Swaps the positions of the two specified rows
    fun swapRows(a: Int, b: Int) {
        val temp = data[a]
        data[a] = data[b]
        data[b] = temp
    }

    // Returns the number of values associated with the specified attribute (or column)
    // 0 = continuous, 2 = binary, 3 = ternary, etc.
    fun valueCount(attr: Int) = enumsToStrs[attr].size

    // Copies that matrix
    fun copy(that: Matrix) {
        setSize(that.rows, that.cols)
        copyBlock(0, 0, that, 0, 0, that.rows, that.cols)
    }

    // Returns the mean of the elements in the specified column. (Elements with the value UNKNOWN_VALUE are ignored.)
    fun columnMean(col: Int): Double {
        var sum = 0.0
        var count = 0
        for (list in data) {
            val v = list[col]
            if (v != UNKNOWN_VALUE) {
                sum += v
                count++
            }
        }

        return sum / count
    }

    // Returns the minimum element in the specified column. (Elements with the value UNKNOWN_VALUE are ignored.)
    fun columnMin(col: Int): Double {
        var min = MAX_VALUE
        for (list in data) {
            val v = list[col]
            if (v != UNKNOWN_VALUE)
                min = min(min, v)
        }

        return min
    }

    // Returns the maximum element in the specified column. (Elements with the value UNKNOWN_VALUE are ignored.)
    fun columnMax(col: Int): Double {
        var max = -MAX_VALUE
        for (list in data) {
            val v = list[col]
            if (v != UNKNOWN_VALUE)
                max = max(max, v)
        }

        return max
    }

    // Returns the most common value in the specified column. (Elements with the value UNKNOWN_VALUE are ignored.)
    fun mostCommonValue(col: Int): Double {
        val counts = HashMap<Double, Int>()
        for (list in data) {
            val v = list[col]
            if (v != UNKNOWN_VALUE) {
                var result: Int? = counts[v]
                if (result == null) result = 0

                counts[v] = result + 1
            }
        }

        var valueCount = 0
        var value = 0.0
        for ((key, value1) in counts) {
            if (value1 > valueCount) {
                value = key
                valueCount = value1
            }
        }

        return value
    }

    // Copies the specified rectangular portion of that matrix, and puts it in the specified location in this matrix.
    fun copyBlock(
        destRow: Int,
        destCol: Int,
        that: Matrix,
        rowBegin: Int,
        colBegin: Int,
        rowCount: Int,
        colCount: Int
    ) {
        if (destRow + rowCount > rows || destCol + colCount > cols)
            throw outOfRangeDestinationMatrixException
        if (rowBegin + rowCount > that.rows || colBegin + colCount > that.cols)
            throw outOfRangeSourceMatrixException

        // Copy the specified region of meta-data
        repeat(colCount) { c ->
            attrNames[destCol + c] = that.attrNames[colBegin + c]
            strsToEnums[destCol + c] = HashMap(that.strsToEnums[colBegin + c])
            enumsToStrs[destCol + c] = HashMap(that.enumsToStrs[colBegin + c])
        }

        // Copy the specified region of data
        repeat(rowCount) { r ->
            repeat(colCount) { c ->
                row(destRow + r)[destCol + c] = that.row(rowBegin + r)[colBegin + c]
            }
        }
    }

    // Sets every element in the matrix to the specified value.
    fun setAll(v: Double) {
        for (vec in data) {
            for (i in vec.indices)
                vec[i] = v
        }
    }

    // Sets every element in the matrix to the specified value.
    operator fun times(scalar: Double) {
        for (vec in data) {
            data.forEach { row ->
                row.map { e ->
                    e * scalar }
            }
        }
    }

    // Sets this to the identity matrix.
    fun setToIdentity() {
        setAll(0.0)
        data.mapIndexed { c, row -> row[c] = 1.0 }
    }

    // Throws an exception if that has a different number of columns than
    // this, or if one of its columns has a different number of values.
    fun checkCompatibility(that: Matrix) {
        if (that.cols != cols)
            throw IllegalArgumentException("Matrices have different number of columns")

        repeat(cols) { i ->
            if (valueCount(i) != that.valueCount(i))
                throw IllegalArgumentException("Column $i has mis-matching number of values.")
        }
    }

    private class SortComparator(var column: Int, var ascending: Boolean) : Comparator<DoubleArray> {
        override fun compare(a: DoubleArray, b: DoubleArray): Int {
            if (a[column] < b[column])
                return if (ascending) -1 else 1
            if (a[column] > b[column])
                return if (ascending) 1 else -1
            return 0
        }
    }

    fun sort(column: Int, ascending: Boolean) = data.sortWith(SortComparator(column, ascending))

    companion object {
        operator fun invoke(rows: Int, cols: Int) = Matrix().apply { setSize(rows, cols) }

        operator fun invoke(that: Matrix) = Matrix().apply {
            filename = that.filename
            setSize(that.rows, that.cols)
            copyBlock(0, 0, that, 0, 0, that.rows, that.cols) // (copies the meta data too)
        }

        operator fun invoke(n: Json) = Matrix().apply {
            val rowCount = n.size
            val colCount = n[0].size
            setSize(rowCount, colCount)
            repeat(rowCount) { r ->
                repeat(colCount) { c ->
                    row(r)[c] = n[r].double(c)
                }
            }
        }

        // Used to represent elements in the matrix for which the value is not known.
        const val UNKNOWN_VALUE = -1e308

        private val outOfRangeSourceMatrixException = IllegalArgumentException("Out of range for source matrix.")
        private val outOfRangeDestinationMatrixException = IllegalArgumentException("Out of range for destination matrix.")
        private val incompatibleRowColSizeException = IllegalArgumentException("Row size differs from the number of columns in this matrix.")
        private val noColumnsException = IllegalArgumentException("You must add some columns before you add any rows.")
    }
}