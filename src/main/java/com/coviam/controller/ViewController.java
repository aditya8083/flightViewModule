package com.coviam.controller;

import com.coviam.entity.FlightSearchResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @RequestMapping(value = "/saveFlightData", method = RequestMethod.GET)
    public ModelAndView saveFlightData()
    {
        ModelAndView mav = new ModelAndView("saveFlightData");
        return mav;
    }

    @RequestMapping("/hello")
    public String sayHello(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

     /* @RequestMapping("/getFlightDetail")
    public ModelAndView getFlightDetail()
    {
        ModelAndView mav = new ModelAndView("getFlightDetail", "command", new FlightSearchResponse());
        return mav;
    }*/

}