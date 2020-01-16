package com.vp.list


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() { // Context of the app under test.
        val appContext =
            InstrumentationRegistry.getTargetContext()
        assertEquals("com.vp.list.test", appContext.packageName)
    }
}