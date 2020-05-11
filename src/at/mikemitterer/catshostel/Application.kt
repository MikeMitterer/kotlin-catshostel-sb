package at.mikemitterer.catshostel

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @since 08.05.20, 21:50
 */
// @SpringBootApplication
// class Application
//
// fun main(args: Array<String>) {
//     runApplication<Application>(*args)
// }

@SpringBootApplication
class Application {
    // @Bean
    // fun commandLineRunner(ctx: ApplicationContext): CommandLineRunner {
    //     return CommandLineRunner { args: Array<String?>? ->
    //         println("Let's inspect the beans provided by Spring Boot:")
    //         val beanNames = ctx.beanDefinitionNames
    //         Arrays.sort(beanNames)
    //         for (beanName in beanNames) {
    //             println(beanName)
    //         }
    //     }
    // }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}