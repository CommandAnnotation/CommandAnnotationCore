package skywolf46.commandannotation.v4.test.suites

import org.junit.runners.Suite.SuiteClasses
import skywolf46.commandannotation.v4.test.CommandTest
import skywolf46.commandannotation.v4.test.ExceptionTest
import skywolf46.commandannotation.v4.test.ReflectionCommandTest

@SuiteClasses(
    CommandTest::class,
    ExceptionTest::class,
    ReflectionCommandTest::class
)
class CommandTestSuites