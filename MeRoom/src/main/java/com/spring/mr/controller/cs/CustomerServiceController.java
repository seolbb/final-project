package com.spring.mr.controller.cs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.spring.mr.controller.user.UserController;
import com.spring.mr.service.cs.AnswerService;
import com.spring.mr.service.cs.EventService;
import com.spring.mr.service.cs.FAService;
import com.spring.mr.service.cs.NewsService;
import com.spring.mr.service.cs.PromotionService;
import com.spring.mr.service.cs.QuestionService;
import com.spring.mr.vo.cs.AnswerVO;
import com.spring.mr.vo.cs.EventVO;
import com.spring.mr.vo.cs.FAVO;
import com.spring.mr.vo.cs.NewsVO;
import com.spring.mr.vo.cs.PromotionVO;
import com.spring.mr.vo.cs.QuestionVO;
import com.spring.mr.vo.user.UserVO;

@Controller
@SessionAttributes({ "answer","event","eventList","fa","faList", "news", "newsList", "promotion", "promotionList",
		"question"})
public class CustomerServiceController {

	@Autowired
	private AnswerService answerService;
	@Autowired
	private EventService eventService;
	@Autowired
	private FAService faService;
	@Autowired
	private NewsService newsService;
	@Autowired
	private PromotionService promotionService;
	@Autowired
	private QuestionService questionService;
	
	
	

	public CustomerServiceController() {
		System.out.println("CustomerServiceController() ?????? ?????? ");
	}

	
	
	
	//?????? ?????? ??????
	
	@RequestMapping("/getAnswer.do")
	public String getAnswer(AnswerVO vo, Model model) {

		System.out.println(">>?????? ?????? ????????????");
		AnswerVO answer = answerService.getAnswer(vo);
		System.out.println("::getAnswer answer : " + answer);
		model.addAttribute("answer", answer);
		return "answerdetail.jsp";

	}

	//?????? ?????? ?????? 
	
	@RequestMapping("/getAnswerList.do")
	public String getAnswerList(AnswerVO vo, Model model) {
		System.out.println(">>?????? ?????? ????????????");
		System.out.println(":: getAnswerList() vo :" + vo);

		List<AnswerVO> answerList = answerService.getAnswerList();

		model.addAttribute("answerList", answerList);
		return "answerlist.jsp";

	}

	//?????? ?????? 
	
	@RequestMapping("/insertAnswer.do")
	public String insertAnswer(AnswerVO vo)  {
		System.out.println(">>?????? ?????? vo : " + vo);
		int insertResult = answerService.insertAnswer(vo);
		if (insertResult == 1) {
			System.out.println("?????? ?????? ??????");
			QuestionVO qvo = new QuestionVO();
			qvo.setqIdx(vo.getqIdx2());
			qvo.setqYn("Y");
			questionService.updateQuestionStatus(qvo);
			
		}
		return "redirect:/getQuestionList.do";
	}

	//?????? ?????? 
	
	
	@RequestMapping("/updateAnswer.do")
	public String updateAnswer (@ModelAttribute("answer") AnswerVO vo) {
		 
		System.out.println(">>> ?????? ??????");
		System.out.println("update vo : " +vo);
		answerService.updateAnswer(vo);
		 return "getAnswerList.do"; 
	}

	//?????? ?????? 
	
	
	@RequestMapping("/deleteAnswer.do")
	public String deleteAnswer( AnswerVO vo) {
	    System.out.println(">>> ?????? ??????");
	    answerService.deleteAnswer(vo);
	    return "getAnswerList.do";
		
		
	}
	
	//-----------event ----------------
	//????????? ????????? ?????? ?????? 
	
	@RequestMapping("/getJsonAnswer.do")
	@ResponseBody
	public AnswerVO getJsonAnswer(@RequestBody AnswerVO vo) { //@RequestBody post?????? ?????? ????????? ??????
		System.out.println("getJsonAnswer() vo : " + vo);
		AnswerVO answer = answerService.getAnswer(vo);
		System.out.println("getJsonAnswer() answer : " + answer);
			   
		return answer;
	}
	
	
	@RequestMapping("/getEvent.do")
	public String getEvent(EventVO vo, Model model) {

		System.out.println(">>????????? ?????? ?????? ????????????");
		EventVO event = eventService.getEvent(vo);
		System.out.println("::getEvent event : " + event);

		model.addAttribute("event", event);
		return "eventdetail.jsp";
	}
	
	// (?????????) ????????? ????????? ?????? ??????  
	@RequestMapping("/getEventAdmin.do")
	@ResponseBody
	public EventVO getEventAdmin(EventVO vo, Model model) {

		System.out.println(">>????????? ?????? ?????? ????????????");
		EventVO event = eventService.getEvent(vo);
		System.out.println("::getEvent event : " + event);

		model.addAttribute("event", event);
		return event;
	}

	//????????? ?????? ??????
	@RequestMapping("/getEventList.do")
	public String getEventList(EventVO vo, Model model) {
		System.out.println(">>????????? ?????? ?????? ????????????");
		System.out.println(":: getEventList() vo : ");

		List<EventVO> eventList = eventService.getEventList();

		model.addAttribute("eventList", eventList);
		return "eventlist.jsp";

	}
	
	// (?????????) ????????? ?????? ?????? 
	@RequestMapping("/getEventListAdmin.do")
	public String getEventListAdmin(EventVO vo, Model model) {
		System.out.println(">>????????? ?????? ?????? ????????????");
		System.out.println(":: getEventList() vo " + vo);

		List<EventVO> eventList = eventService.getEventList(vo);

		model.addAttribute("eventList", eventList);
		return "/views/management/mgmtInfo/event/eventMain.jsp";

	}

	// (?????????) ????????? ????????? ??????
	@RequestMapping("/insertEvent.do")
	public String insertEvent(EventVO vo) throws IllegalStateException, IOException {
		System.out.println(">>????????? ??????");
		System.out.println("vo : " + vo);


		eventService.insertEvent(vo);
		return "redirect:getEventList.do";
	}
	
	// (?????????) ????????? ????????? ??????
	@RequestMapping("/updateEvent.do")
	public String updateEvent(@ModelAttribute("event") EventVO vo) {

		System.out.println(">>> ????????? ??????");
		System.out.println("update vo : " + vo);
		eventService.updateEvent(vo);
		return "redirect:getEventList.do";

	}

	// (?????????) ????????? ????????? ?????? 
	@RequestMapping("/deleteEvent.do")
	public String deleteEvent(EventVO vo) {

		System.out.println(">>> ????????? ??????");
		eventService.deleteEvent(vo);
		return "redirect:getEventList.do";

	}

	// (?????????) ????????? ?????? ??? ?????? ????????? 
	@PostMapping(value = "/AjaxAction.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<EventVO>> uploadAjaxActionPOST(MultipartFile[] uploadFile) {
		System.out.println("uploadAjaxAction.do??? ?????????");

		
		String uploadFolder = "c:\\eventupload";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		String datePath = str.replace("-", "\\\\");
		File uploadPath = new File(uploadFolder, datePath);
		
		
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		List<EventVO> list = new ArrayList<EventVO>();
		
		for (MultipartFile multipartFile : uploadFile ) {
			
			EventVO vo = new EventVO();
			
			// ????????????
			String uploadFileName = multipartFile.getOriginalFilename();
			
			// uuid ?????? ?????? ??????   /  uuid??? ?????? ?????? ?????????
			String uuid = UUID.randomUUID().toString();
			
			uploadFileName = uuid + "_" + uploadFileName; // ?????? ?????? ??????
			
			// ?????? ??????, ?????? ????????? ?????? File ??????
			File saveFile = new File(uploadPath, uploadFileName);
			 
			// ?????? ??????
			try {
				multipartFile.transferTo(saveFile);
				
				// ??????
				File thumbnailFile = new File(uploadPath, "s_" + uploadFileName); // ?????? ????????? ??????
				vo.setEvThumbnail(datePath.toString() + "/s_" + uploadFileName);
				BufferedImage bo_image = ImageIO.read(saveFile);  //buffered original image
				
				// ?????? 
				double ratio = 3;
				
				// ?????? , ??????
				int width = (int)(bo_image.getWidth() / ratio); // ?????????
				int height  = (int)(bo_image.getHeight() / ratio); // ?????????
				
				BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				
				Graphics2D graphic = bt_image.createGraphics();
				graphic.drawImage(bo_image, 0, 0, width, height, null);
				// 1. ??????????????? ?????? ?????????  2. x???  3. y???  4. ?????? 5. ?????? 6. ImageObserver ??????(????????? ???????????? ????????? ??????  ??????????????? null)
				
				ImageIO.write(bt_image, "jpg", thumbnailFile);
				

			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			list.add(vo);
		}
		ResponseEntity<List<EventVO>> result = new ResponseEntity<List<EventVO>>(list, HttpStatus.OK);
		System.out.println("result : " + result);
		return result;
	}
	
	
	
	// (?????????) ????????? ?????? ??? ????????? ?????? ??????  
	@GetMapping("/screenoutput.do")
	@ResponseBody
	public ResponseEntity<byte[]> getImage(String fileName) {
		System.out.println("getImage()..............");
		
		File file = new File("c:\\eventupload\\" + fileName);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("content-type", Files.probeContentType(file.toPath()));			
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK); //????????? ??????
			
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		return result;
	}

	
	// ????????? ?????? ??????
	
	// (?????????) ????????? ?????? ??? ????????? ?????? ??????
		@PostMapping("/deleteImageFile.do")
		@ResponseBody
		public ResponseEntity<String> deleteFile(String fileName) {
			File file = null;
			System.out.println("deleteFiledo ???????????? : " + fileName);
			try {
				
				// ????????? ?????? ??????
				file = new File("c:\\eventupload\\" + URLDecoder.decode(fileName, "UTF-8"));
				file.delete();
				
				// ?????? ?????? ??????
				String originFileName =  file.getAbsolutePath().replace("s_", "");
				file = new File(originFileName);
				file.delete();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				return new ResponseEntity<String>("fail", HttpStatus.NOT_IMPLEMENTED);
			}	
			
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
			
		}
	
	//	Summernote API ????????? ???????????? ????????? ???????????? ???????????? ajax
		@RequestMapping(value="/ImageFile.do", produces="application/json; charset=utf-8")
		@ResponseBody
		public String ImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
			JsonObject jsonObject = new JsonObject();
			
			
			String fileRoot = "c:\\summernote_image\\";  //??????????????? ????????? ????????? ??? 
			
			
			//??????????????? ??????
			String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
			System.out.println("contextRoot : " + contextRoot);
			//String fileRoot = contextRoot + "resource/fileupload/";
			
			System.out.println("fileRoot : " + fileRoot);
			
			String originalFileName = multipartFile.getOriginalFilename();  //???????????? ?????????
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));  //?????? ?????????
			String savedFileName = UUID.randomUUID() + extension;  //????????? ?????????
			File targetFile = new File(fileRoot + savedFileName);
			
			try {
				InputStream fileStream = multipartFile.getInputStream();
				FileUtils.copyInputStreamToFile(fileStream, targetFile);  //????????????
				// contextroot + resources + ????????? ?????? ?????????
				jsonObject.addProperty("url", "/summernote_image/"+savedFileName);
				jsonObject.addProperty("responseCode", "success");
				
			} catch (IOException e){
				FileUtils.deleteQuietly(targetFile);  //????????? ?????? ??????
				jsonObject.addProperty("responseCode", "error");
				e.printStackTrace();
			}
			String a = jsonObject.toString();
			System.out.println("jsonObject : " + a);
			return a;
		}
	   
	 
	
	
	//--------?????? ?????? ?????? -----------
	//?????? ?????? ?????? ????????? ?????? ?????? ?????? 
	@RequestMapping("/getFA.do")
	public String getFA(FAVO vo, Model model) {

		System.out.println(">>???????????? ?????? ?????? ??????");
		FAVO fa = faService.getFA(vo);
		System.out.println("::getFA fa : " + fa);

		model.addAttribute("fa", fa);
		return "supportdetail.jsp";
	}

	// (?????????) ?????? ?????? ?????? ????????? ?????? ?????? ??????  
	@RequestMapping("/getFAAdmin.do")
	@ResponseBody
	public FAVO getFAAdmin(FAVO vo ,Model model) {
		
		System.out.println(">>???????????? ?????? ?????? ??????");
		FAVO fa = faService.getFA(vo);
		System.out.println("::getFA fa : " + fa);
		
		model.addAttribute("fa", fa); 
		return fa;
	}
	
	//?????? ?????? ?????? ????????? ?????? ??????
	@RequestMapping("/getFAList.do")
	public String getFAList(Model model) {
		System.out.println(">>???????????? ?????? ?????? ??????");

		List<FAVO> faList = faService.getFAList();

		model.addAttribute("faList", faList);
		return "support.jsp";

	}

	
	// (?????????) ?????? ?????? ?????? ?????? ??????	
	@RequestMapping("/getFAListAdmin.do")
	public String getFAListAdmin(Model model) {
		System.out.println(">>???????????? ?????? ?????? ??????");
		
		List<FAVO> faList = faService.getFAList();
		
		model.addAttribute("faList", faList);
		return "/views/management/mgmtInfo/faq/FAQMain.jsp";

	}
	
	// (?????????) ?????? ?????? ?????? ??????
	@RequestMapping("/insertFA.do")
	public String insertFA(FAVO vo) throws IllegalStateException, IOException {

		System.out.println(">>???????????? ?????? ??????");
		System.out.println("vo : " + vo);
		
		faService.insertFA(vo);
		return "redirect:getFAListAdmin.do";
	}

	@RequestMapping("/updateFA.do")
	public String updateFA (@ModelAttribute("fa") FAVO vo) {
		 
		System.out.println(">>> ???????????? ?????? ??????");
		System.out.println("update vo : " +vo);
		faService.updateFA(vo);
         return "redirect:getFAListAdmin.do";
		
	}
	
	// (?????????) ???????????? ?????? ??????
	@RequestMapping("/deleteFA.do")
	public String deleteFA( FAVO vo) {
		 
	    System.out.println(">>> ???????????? ?????? ??????");
	    faService.deleteFA(vo);
 		return "redirect:getFAListAdmin.do";
		
		
	}

	// Summernote API ????????? ???????????? ????????? ???????????? ???????????? ajax
	@RequestMapping(value = "/Fileupload.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String Fileupload(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
		JsonObject jsonObject = new JsonObject();

		System.out.println("controller ?????????");

		String fileRoot = "c:\\summernote_image\\"; // ??????????????? ????????? ????????? ???

		// ??????????????? ??????
		String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
		System.out.println("contextRoot : " + contextRoot);
		// String fileRoot = contextRoot + "resource/fileupload/";

		System.out.println("fileRoot : " + fileRoot);

		String originalFileName = multipartFile.getOriginalFilename(); // ???????????? ?????????
		String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // ?????? ?????????
		String savedFileName = UUID.randomUUID() + extension; // ????????? ?????????
		File targetFile = new File(fileRoot + savedFileName);

		try {
			InputStream fileStream = multipartFile.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, targetFile); // ????????????
			// contextroot + resources + ????????? ?????? ?????????
			jsonObject.addProperty("url", "/summernote_image/" + savedFileName);
			jsonObject.addProperty("responseCode", "success");

		} catch (IOException e) {
			FileUtils.deleteQuietly(targetFile); // ????????? ?????? ??????
			jsonObject.addProperty("responseCode", "error");
			e.printStackTrace();
		}
		String a = jsonObject.toString();
		System.out.println("jsonObject : " + a);
		return a;
	}
	
	
	//-------news ---------------
	//?????? ????????? ?????? ?????? ??????
	@RequestMapping("/getNews.do")
	public String getNews(NewsVO vo, Model model) {

		System.out.println(">>????????? ?????? ?????? ????????????");
		NewsVO news = newsService.getNews(vo);
		System.out.println("::getNews news : " + news);

		model.addAttribute("news", news);
		return "newsdetail.jsp";
	}
	
	// (?????????) ??????????????? ?????? ?????? ??????
	@RequestMapping("/getNewsAdmin.do")
	@ResponseBody
	public NewsVO getNewsAdmin(NewsVO vo, Model model) {
		System.out.println(">>????????? ?????? ?????? ????????????");
		NewsVO news = newsService.getNews(vo);
		System.out.println("::getNews news : " + news);
		model.addAttribute("news", news);
		
		return news;
	}
	
	

	//?????? ????????? ?????? ?????? ?????? 
	@RequestMapping("/getNewsList.do")
	public String getNewsList(NewsVO vo, Model model) {
		System.out.println(">>????????? ?????? ?????? ????????????");
		System.out.println(":: getNewsList() vo " + vo);

		List<NewsVO> newsList = newsService.getNewsList(vo);

		model.addAttribute("newsList", newsList);
		return "newslist.jsp";

	}
	
	@RequestMapping("/getNewsListAdmin.do")
	public String getNewsListAdmin(Model model) {
		System.out.println(">>????????? ?????? ?????? ????????????");
		List<NewsVO> newsList = newsService.getNewsList();
		model.addAttribute("newsList", newsList);
		return "/views/management/mgmtInfo/news/newsMain.jsp";

	}
	
	
	// (?????????) ?????? ????????? ?????? 
		@RequestMapping("/insertNews.do")
		public String insertNews(NewsVO vo) throws IllegalStateException, IOException {

			System.out.println(">>????????? ??????");
			System.out.println("vo : " + vo);


			newsService.insertNews(vo);
			return "redirect:getNewsListAdmin.do";
		}

	//?????? ????????? ?????? 
		@RequestMapping("/updateNews.do")
		public String updateNews(@ModelAttribute("news") NewsVO vo) {

			System.out.println(">>> ????????? ??????");
			System.out.println("update vo : " + vo);
			newsService.updateNews(vo);
			return "redirect:getNewsListAdmin.do";

		}
		
		// (?????????) ?????? ????????? ?????? 
		@RequestMapping("/deleteNews.do")
		public String deleteNews(NewsVO vo) {

			System.out.println(">>> ????????? ??????");
			newsService.deleteNews(vo);
			return "redirect:getNewsListAdmin.do";

		}
	
	
	
		// (?????????) ?????? ????????? ?????? ??? ?????? ?????????
		@PostMapping(value = "/uploadAjaxPOST.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		@ResponseBody
		public ResponseEntity<List<NewsVO>> uploadAjaxPOST(MultipartFile[] uploadFile) {
			System.out.println("uploadAjaxAction.do??? ?????????");

			
			String uploadFolder = "c:\\newsupload";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String str = sdf.format(date);
			String datePath = str.replace("-", "\\\\");
			File uploadPath = new File(uploadFolder, datePath);
			
			
			if (uploadPath.exists() == false) {
				uploadPath.mkdirs();
			}
			
			List<NewsVO> list = new ArrayList<NewsVO>();
			
			for (MultipartFile multipartFile : uploadFile ) {
				
				NewsVO vo = new NewsVO();
				
				// ????????????
				String uploadFileName = multipartFile.getOriginalFilename();
				
				// uuid ?????? ?????? ??????   /  uuid??? ?????? ?????? ?????????
				String uuid = UUID.randomUUID().toString();
				
				uploadFileName = uuid + "_" + uploadFileName; // ?????? ?????? ??????
				
				// ?????? ??????, ?????? ????????? ?????? File ??????
				File saveFile = new File(uploadPath, uploadFileName);
				 
				// ?????? ??????
				try {
					multipartFile.transferTo(saveFile);
					
					// ??????
					File thumbnailFile = new File(uploadPath, "s_" + uploadFileName); // ?????? ????????? ??????
					vo.setNeThumbnail(datePath.toString() + "/s_" + uploadFileName);
					BufferedImage bo_image = ImageIO.read(saveFile);  //buffered original image
					
					// ?????? 
					double ratio = 3;
					
					// ?????? , ??????
					int width = (int)(bo_image.getWidth() / ratio); // ?????????
					int height  = (int)(bo_image.getHeight() / ratio); // ?????????
					
					BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
					
					Graphics2D graphic = bt_image.createGraphics();
					graphic.drawImage(bo_image, 0, 0, width, height, null);
					// 1. ??????????????? ?????? ?????????  2. x???  3. y???  4. ?????? 5. ?????? 6. ImageObserver ??????(????????? ???????????? ????????? ??????  ??????????????? null)
					
					ImageIO.write(bt_image, "jpg", thumbnailFile);
					

				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				list.add(vo);
			}
			ResponseEntity<List<NewsVO>> result = new ResponseEntity<List<NewsVO>>(list, HttpStatus.OK);
			System.out.println("result : " + result);
			return result;
		}
	
	
		// (?????????)  ?????? ?????? ??? ????????? ?????? ??????
		@GetMapping("/screenplay.do")
		@ResponseBody
		public ResponseEntity<byte[]> getThumbnail(String fileName) {
			System.out.println("getThumbnail()..............");
			
			File file = new File("c:\\newsupload\\" + fileName);
			
			ResponseEntity<byte[]> result = null;
			
			try {
				HttpHeaders header = new HttpHeaders();
				
				header.add("content-type", Files.probeContentType(file.toPath()));			
				result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK); 
				
			} catch (IOException e) {
				e.printStackTrace();
			
			}
			return result;
		}

	
	// ????????? ?????? ??????
	
		// (?????????) ?????? ?????? ??? ????????? ?????? ?????? 
		@PostMapping("/deleteThumbnail.do")
		@ResponseBody
		public ResponseEntity<String> deleteImageFile(String fileName) {
			File file = null;
			System.out.println("deleteFilefo ???????????? : " + fileName);
			try {
				
				// ????????? ?????? ??????
				file = new File("c:\\newsupload\\" + URLDecoder.decode(fileName, "UTF-8"));
				file.delete();
				
				// ?????? ?????? ??????
				String originFileName =  file.getAbsolutePath().replace("s_", "");
				file = new File(originFileName);
				file.delete();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				return new ResponseEntity<String>("fail", HttpStatus.NOT_IMPLEMENTED);
			}	
			
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
			
		}
	
	

		
		// (?????????) ?????? ?????? ???????????? ?????? ?????? 
		@RequestMapping(value = "/uploadImageFile.do", produces = "application/json; charset=utf-8")
		@ResponseBody
		public String uploadImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
			JsonObject jsonObject = new JsonObject();

			System.out.println("controller ?????????");

			String fileRoot = "c:\\summernote_image\\"; // ??????????????? ????????? ????????? ???

			// ??????????????? ??????
			String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
			System.out.println("contextRoot : " + contextRoot);
			// String fileRoot = contextRoot + "resource/fileupload/";

			System.out.println("fileRoot : " + fileRoot);

			String originalFileName = multipartFile.getOriginalFilename(); // ???????????? ?????????
			String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // ?????? ?????????
			String savedFileName = UUID.randomUUID() + extension; // ????????? ?????????
			File targetFile = new File(fileRoot + savedFileName);

			try {
				InputStream fileStream = multipartFile.getInputStream();
				FileUtils.copyInputStreamToFile(fileStream, targetFile); // ????????????
				// contextroot + resources + ????????? ?????? ?????????
				jsonObject.addProperty("url", "/summernote_image/" + savedFileName);
				jsonObject.addProperty("responseCode", "success");

			} catch (IOException e) {
				FileUtils.deleteQuietly(targetFile); // ????????? ?????? ??????
				jsonObject.addProperty("responseCode", "error");
				e.printStackTrace();
			}
			String a = jsonObject.toString();
			System.out.println("jsonObject : " + a);
			return a;
		}

	
	
	
	
	//--------?????? ?????? -----------
	
	//???????????? ????????? ?????? ?????? 
	@RequestMapping("/getPromotion.do")
	public String getPromotion(PromotionVO vo, Model model) {

		System.out.println(">>????????? ?????? ?????? ????????????");
		PromotionVO promotion = promotionService.getPromotion(vo);
		System.out.println("::getPromotion promotion : " + promotion);

		model.addAttribute("promotion", promotion);
		return "promotiondetail.jsp";
	}
	
	// (?????????) ???????????? ????????? ?????? ?????? 
	@RequestMapping("/getPromotionAdmin.do")
	@ResponseBody
	public PromotionVO getPromotionAdmin(PromotionVO vo, Model model) {

		System.out.println(">>????????? ?????? ?????? ????????????");
		PromotionVO promotion = promotionService.getPromotion(vo);
		System.out.println("::getPromotion promotion : " + promotion);

		model.addAttribute("promotion", promotion);
		return promotion;
	}

	//???????????? ?????? ?????? 
	@RequestMapping("/getPromotionList.do")
	public String getPromotionList(PromotionVO vo, Model model) {
		System.out.println(">>????????? ?????? ?????? ????????????");
		System.out.println(":: getPromotionList() : ");

		List<PromotionVO> promotionList = promotionService.getPromotionList();

		model.addAttribute("promotionList", promotionList);
		return "promotionlist.jsp";

	}
	
	// (?????????) ???????????? ?????? ?????? 
		@RequestMapping("/getPromotionListAdmin.do")
		public String getPromotionListAdmin(PromotionVO vo, Model model) {
			System.out.println(">>????????? ?????? ?????? ????????????");
			System.out.println(":: getPromotionList() vo " + vo);

			List<PromotionVO> promotionList = promotionService.getPromotionList(vo);

			model.addAttribute("promotionList", promotionList);
			return "/views/management/mgmtInfo/promotion/promotionMain.jsp";

		}
	
	//???????????? ????????? ?????? 
	@RequestMapping("/insertPromotion.do")
	public String insertPromotion(PromotionVO vo) throws IllegalStateException, IOException {

		System.out.println(">>????????? ??????");
		System.out.println("vo : " + vo);

		promotionService.insertPromotion(vo);
		return "redirect:getPromotionListAdmin.do";
	}
	
	//???????????? ????????? ??????
	@RequestMapping("/updatePromotion.do")
	public String updatePromotion(@ModelAttribute("promotion") PromotionVO vo) {

		System.out.println(">>> ????????? ??????");
		System.out.println("update vo : " + vo);
		promotionService.updatePromotion(vo);
		return "redirect:getPromotionListAdmin.do";

	}
	
	//???????????? ????????? ?????? 
	@RequestMapping("/deletePromotion.do")
	public String deletePromotion(PromotionVO vo) {

		System.out.println(">>> ????????? ??????");
		promotionService.deletePromotion(vo);
		return "redirect:getPromotionListAdmin.do";

	}

	// (?????????) ???????????? ?????? ??? ?????? ?????????
		@PostMapping(value = "/uploadAjax.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		@ResponseBody
		public ResponseEntity<List<PromotionVO>> uploadAjax(MultipartFile[] uploadFile) {
			System.out.println("uploadAjax.do??? ?????????");

			
			String uploadFolder = "c:\\promotionupload";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String str = sdf.format(date);
			String datePath = str.replace("-", "\\\\");
			File uploadPath = new File(uploadFolder, datePath);
			
			
			if (uploadPath.exists() == false) {
				uploadPath.mkdirs();
			}
			
			List<PromotionVO> list = new ArrayList<PromotionVO>();
			
			for (MultipartFile multipartFile : uploadFile ) {
				
				PromotionVO vo = new PromotionVO();
				
				// ????????????
				String uploadFileName = multipartFile.getOriginalFilename();
				
				// uuid ?????? ?????? ??????   /  uuid??? ?????? ?????? ?????????
				String uuid = UUID.randomUUID().toString();
				
				uploadFileName = uuid + "_" + uploadFileName; // ?????? ?????? ??????
				
				// ?????? ??????, ?????? ????????? ?????? File ??????
				File saveFile = new File(uploadPath, uploadFileName);
				 
				// ?????? ??????
				try {
					multipartFile.transferTo(saveFile);
					
					// ??????
					File thumbnailFile = new File(uploadPath, "s_" + uploadFileName); // ?????? ????????? ??????
					vo.setPrThumbnail(datePath.toString() + "/s_" + uploadFileName);
					BufferedImage bo_image = ImageIO.read(saveFile);  //buffered original image
					
					// ?????? 
					double ratio = 3;
					
					// ?????? , ??????
					int width = (int)(bo_image.getWidth() / ratio); // ?????????
					int height  = (int)(bo_image.getHeight() / ratio); // ?????????
					
					BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
					
					Graphics2D graphic = bt_image.createGraphics();
					graphic.drawImage(bo_image, 0, 0, width, height, null);
					// 1. ??????????????? ?????? ?????????  2. x???  3. y???  4. ?????? 5. ?????? 6. ImageObserver ??????(????????? ???????????? ????????? ??????  ??????????????? null)
					
					ImageIO.write(bt_image, "jpg", thumbnailFile);
					

				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				list.add(vo);
			}
			ResponseEntity<List<PromotionVO>> result = new ResponseEntity<List<PromotionVO>>(list, HttpStatus.OK);
			System.out.println("result : " + result);
			return result;
		}
	
	
	// ????????? ?????? ??????
	@GetMapping("/pictureplay.do")
	@ResponseBody
	public ResponseEntity<byte[]> getpicture(String fileName) {
		System.out.println("getpicture()..............");
		
		File file = new File("c:\\promotionupload\\" + fileName);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("content-type", Files.probeContentType(file.toPath()));			
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK); 
			
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		return result;
	}

	
	// ????????? ?????? ??????
	
	@PostMapping("/deletepicture.do")
	@ResponseBody
	public ResponseEntity<String> deletepicture(String fileName) {
		File file = null;
		System.out.println("deletepicture ???????????? : " + fileName);
		try {
			
			// ????????? ?????? ??????
			file = new File("c:\\promotionupload\\" + URLDecoder.decode(fileName, "UTF-8"));
			file.delete();
			
			// ?????? ?????? ??????
			String originFileName =  file.getAbsolutePath().replace("s_", "");
			file = new File(originFileName);
			file.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<String>("fail", HttpStatus.NOT_IMPLEMENTED);
		}	
		
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
		
	}
	

	
	// (?????????) ???????????? ?????? ??? ???????????? ????????? ?????? 
		@RequestMapping(value = "/uploadFile.do", produces = "application/json; charset=utf-8")
		@ResponseBody
		public String uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
			JsonObject jsonObject = new JsonObject();

			System.out.println("controller ?????????");

			String fileRoot = "c:\\summernote_image\\"; // ??????????????? ????????? ????????? ???

			// ??????????????? ??????
			String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
			System.out.println("contextRoot : " + contextRoot);
			// String fileRoot = contextRoot + "resource/fileupload/";

			System.out.println("fileRoot : " + fileRoot);

			String originalFileName = multipartFile.getOriginalFilename(); // ???????????? ?????????
			String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // ?????? ?????????
			String savedFileName = UUID.randomUUID() + extension; // ????????? ?????????
			File targetFile = new File(fileRoot + savedFileName);

			try {
				InputStream fileStream = multipartFile.getInputStream();
				FileUtils.copyInputStreamToFile(fileStream, targetFile); // ????????????
				// contextroot + resources + ????????? ?????? ?????????
				jsonObject.addProperty("url", "/summernote_image/" + savedFileName);
				jsonObject.addProperty("responseCode", "success");

			} catch (IOException e) {
				FileUtils.deleteQuietly(targetFile); // ????????? ?????? ??????
				jsonObject.addProperty("responseCode", "error");
				e.printStackTrace();
			}
			String a = jsonObject.toString();
			System.out.println("jsonObject : " + a);
			return a;
		}

	
	
	
	//---------????????? ?????? ------------------
	
	//?????? 
		// (?????????) ????????? ?????? ?????? ??? ?????? 
		@ModelAttribute("conditionMap")
		public Map<String, String> searchConditionMap() {
			System.out.println("--->>Map searchConditionMap() ??????");
			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("??????", "TITLE");
			conditionMap.put("??????", "CONTENT");
			return conditionMap;

		}
		

	
	//????????? ?????? ?????? 
		// (?????????) ????????? ?????? ?????? 
		@RequestMapping("/getQuestion.do")
		@ResponseBody
		public QuestionVO getQuestion(QuestionVO vo ,Model model) {
			
			QuestionVO question = questionService.getQuestion(vo);
			System.out.println("question : " + question);
			model.addAttribute("question", question); 
			return question;
		}
	
	@RequestMapping("/getQuestionList.do")
	public String getQuestionList(QuestionVO vo, Model model) {
		if (vo.getAnyTyp() == null) {
				System.out.println("anyTyp is null");
				vo.setAnyTyp("");
			}
		if (vo.getqTitle() == null) { 
			System.out.println("qTitle is null");
				vo.setqTitle(""); 
			}
		if (vo.getqContent() == null) {
			System.out.println("qContent is null");
				vo.setqContent(""); 
			}
		List<QuestionVO> questionList = questionService.getQuestionList(vo);
		System.out.println("qnaList : " + questionList);
		model.addAttribute("questionList", questionList);
		return "/views/management/mgmtInfo/qna/QnAMain.jsp";
	}
	
	//????????? ?????? 
	@RequestMapping("/insertQuestion.do")
	public String insertQuestion(QuestionVO vo)  {

		System.out.println(">>????????? ?????? ??????");
		System.out.println("vo : " + vo);
		
		questionService.insertQuestion(vo);
		
		return "redirect:qnaList.do";
	}
	

	//????????? ?????? 
	@RequestMapping("/deleteQuestion.do")
	public String deleteQuestion(QuestionVO vo) {
		System.out.println("delete vo : " + vo);
	    System.out.println(">>>????????? ?????? ??????");
	    questionService.deleteQuestion(vo);
 		return "redirect:getQuestionList.do";
	}
	
	

	

}
