import com.google.inject.name.{Named, Names}
import com.google.inject.{Singleton, Inject, Guice, AbstractModule}
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import scalaz.Reader
import com.guiceprovider.GuiceProvider._

/**
 * User: travis.stevens@gaiam.com
 * Date: 12/5/13
 */
object GuiceProviderSpec extends Specification {

  case class Spaceship @Inject() @Singleton() (@Named("green") driver: String)
  case class BugTrap @Inject() @Singleton() (@Named("yellow") whoIs: String)

  object Module extends AbstractModule {

    val five = 5
    val greenHornet = "Green Hornet"
    val yellowWasp = "Yellow Wasp"
    val beTrue = true
    val onePointOne = 1.1

    def configure() = {
      bind(classOf[Int]).toInstance(five)
      bind(classOf[String]).annotatedWith(Names.named("green")).toInstance(greenHornet)
      bind(classOf[String]).annotatedWith(Names.named("yellow")).toInstance(yellowWasp)
      bind(classOf[Boolean]).toInstance(beTrue)
      bind(classOf[Double]).toInstance(onePointOne)
//      bind(classOf[Spaceship]).to(classOf[Spaceship])
//      bind(classOf[BugTrap]).to(classOf[BugTrap])

    }

  }

  trait InjectorScope extends Scope {
    val injector = Guice.createInjector(Module)
  }

  "Guice Provider" should {

    "have instance shortcut methods that works" in new InjectorScope {


      injector.instance[Int] mustEqual Module.five
      injector.instance[String]("green") shouldEqual Module.greenHornet
      injector.instance[String]("yellow") mustEqual Module.yellowWasp

    }

    "have single and double, ect. providers " in new InjectorScope {
      val single = Reader[Spaceship,String](ship => ship.driver)
      injector.provider[Spaceship].apply(single) mustEqual Module.greenHornet


      val double = Reader[(Spaceship, BugTrap), (String, String)](st => {
        (st._1.driver, st._2.whoIs)
      })
      injector.provider[Spaceship, BugTrap].apply(double) mustEqual (Module.greenHornet, Module.yellowWasp)

      val triple = Reader[(Spaceship, BugTrap, Int), (String, String, Int)](sbi => {
        (sbi._1.driver, sbi._2.whoIs, sbi._3)
      })
      injector.provider[Spaceship, BugTrap, Int].apply(triple) mustEqual (Module.greenHornet, Module.yellowWasp, Module.five)

      val quad = Reader[(Spaceship, BugTrap, Int, Boolean), (String, String, Int, Boolean)](sbib => {
        (sbib._1.driver, sbib._2.whoIs, sbib._3, sbib._4)
      })
      injector.provider[Spaceship, BugTrap, Int, Boolean].apply(quad) mustEqual
        (Module.greenHornet, Module.yellowWasp, Module.five, Module.beTrue)

      val quint = Reader[(Spaceship, BugTrap, Int, Boolean, Double), (String, String, Int, Boolean, Double)](sbib => {
        (sbib._1.driver, sbib._2.whoIs, sbib._3, sbib._4, sbib._5)
      })
      injector.provider[Spaceship, BugTrap, Int, Boolean, Double].apply(quint) mustEqual
        (Module.greenHornet, Module.yellowWasp, Module.five, Module.beTrue, Module.onePointOne)

    }

    "build traits for readers" in new InjectorScope {
      trait Input {
        val ship: Spaceship
        val trap: BugTrap
        val driver: String
      }

      val fromInput = Reader[Input, (String, String, String)](input => (input.ship.driver, input.trap.whoIs, input.driver))

      injector.buildProvider(i => new Input {
        val driver = i.instance[String]("green")
        val ship = i.instance[Spaceship]
        val trap = i.instance[BugTrap]
      }).apply(fromInput) mustEqual (Module.greenHornet, Module.yellowWasp, Module.greenHornet)

    }

  }
}
