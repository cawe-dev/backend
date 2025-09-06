package org.cawe.dev.backend.infrastructure.adapter.rest.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @GetMapping("/")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping(value = "/redoc", produces = "text/html")
    @ResponseBody
    public String getRedoc() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>ReDoc API Documentation</title>
                    <meta charset="utf-8"/>
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                </head>
                <body>
                    <redoc spec-url='/v3/api-docs'></redoc>
                    <script src="https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js"></script>
                </body>
                </html>
                """;
    }
}
