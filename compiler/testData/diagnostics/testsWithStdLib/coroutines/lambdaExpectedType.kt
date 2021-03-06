// !WITH_NEW_INFERENCE
// !CHECK_TYPE
// !DIAGNOSTICS: -UNUSED_PARAMETER -ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE -UNUSED_VALUE -UNUSED_VARIABLE

fun builder(c: suspend () -> Int) = 1
fun <T> genericBuilder(c: suspend () -> T): T = null!!
fun unitBuilder(c: suspend () -> Unit) = 1
fun emptyBuilder(c: suspend () -> Unit) = 1

fun <T> manyArgumentsBuilder(
        c1: suspend () -> Unit,
        c2: suspend () -> T,
        c3: suspend () -> Int
):T = null!!

fun severalParamsInLambda(c: suspend (String, Int) -> Unit) {}

fun foo() {
    builder({ 1 })
    builder { 1 }

    val x = { 1 }
    builder(<!TYPE_MISMATCH!>x<!>)
    builder({1} <!UNCHECKED_CAST!>as (suspend () -> Int)<!>)

    var i: Int = 1
    i = genericBuilder({ 1 })
    i = genericBuilder { 1 }
    genericBuilder { 1 }
    genericBuilder<Int> { 1 }
    genericBuilder<Int> { <!NI;TYPE_MISMATCH, TYPE_MISMATCH!>""<!> }

    val y = { 1 }
    <!OI;TYPE_INFERENCE_PARAMETER_CONSTRAINT_ERROR!>genericBuilder<!>(<!TYPE_MISMATCH!>y<!>)

    <!NI;UNREACHABLE_CODE!>unitBuilder {}<!>
    <!NI;UNREACHABLE_CODE!>unitBuilder { <!OI;UNUSED_EXPRESSION!>1<!> }<!>
    <!NI;UNREACHABLE_CODE!>unitBuilder({})<!>
    <!NI;UNREACHABLE_CODE!>unitBuilder({ <!OI;UNUSED_EXPRESSION!>1<!> })<!>

    <!NI;UNREACHABLE_CODE!>manyArgumentsBuilder({}, { "" }) { 1 }<!>

    <!NI;UNREACHABLE_CODE!>val s: String = manyArgumentsBuilder({}, { "" }) { 1 }<!>

    <!NI;UNREACHABLE_CODE!>manyArgumentsBuilder<String>({}, { "" }, { 1 })<!>
    <!NI;UNREACHABLE_CODE!>manyArgumentsBuilder<String>({}, { <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!> }, { 2 })<!>

    <!NI;UNREACHABLE_CODE!>severalParamsInLambda { <!NAME_SHADOWING!>x<!>, <!NAME_SHADOWING!>y<!> ->
        x checkType { _<String>() }
        y checkType { _<Int>() }
    }<!>
}
