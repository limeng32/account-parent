package limeng32.mirage.core.web;

import java.util.List;

import limeng32.mirage.core.pojo.Writer;
import limeng32.mirage.core.pojo.condition.WriterCondition;
import limeng32.mirage.core.service.WriterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/index")
public class TestController {

	@Autowired
	private WriterService writerService;

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		return "index";
	}

	@RequestMapping(value = "/testMix")
	public String get(ModelMap mm) {
		WriterCondition wc = new WriterCondition();
		wc.setNameLike("王");
		List<Writer> ret = writerService.selectAll(wc);
		mm.addAttribute("_content", ret);
		return "testController1";
	}

}
