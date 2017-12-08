@RestController
class WebApplication {

    @RequestMapping("/")
    String home() {
        "Hello World!"
    }

}