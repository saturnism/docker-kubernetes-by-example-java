/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.springframework.web.context.request.*

@Grab("thymeleaf-spring4")
@Controller
@SessionAttributes("name")
class HelloWorldController {
    @Autowired
    HelloWorldService service

    @RequestMapping("/")
    def index(Model model) {
      if (model.containsAttribute("name")) {
        def greeting = model.addAttribute("greeting", service.greet(model.asMap().("name")))
      }

      model.addAttribute("ui", [
        "sessionId": RequestContextHolder.currentRequestAttributes().sessionId,
        "hostname": InetAddress.localHost.hostName
      ])
      return "index"
    }

    @RequestMapping("/greet")
    def index(@RequestParam String name, Model model) {
      model.addAttribute("name", name)
      return "redirect:/"
    }
}
