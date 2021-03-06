package com.oumuv.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oumuv.entity.LoginRecordEntity;
import com.oumuv.entity.User;
import com.oumuv.service.LoginRecordService;
import com.oumuv.service.UserService;
import com.oumuv.utils.AccessSiteUtil;
import com.oumuv.utils.MD5Util;

/**
 * @author Administrator user控制层
 */
@Controller
@RequestMapping("/user")
public class UserAction {

	@Resource
	private UserService userService;
	@Autowired
	private LoginRecordService loginRecordService;

	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String shiroLogin(@Param(value = "usename") String username,
			@Param("password") String password,
			@Param("address") String address, HttpServletRequest request,
			ModelMap map, HttpSession session) throws UnsupportedEncodingException {

		Subject subject = SecurityUtils.getSubject();// 获取subject实例
		boolean authenticated = subject.isAuthenticated();// 判断用户是否已经登录
//			SavedRequest request2 = WebUtils.getSavedRequest(request);
//			String requestUrl = request2.getRequestUrl();//获取登录之前的url
		if(authenticated){
			return "index";//已经登录直接跳转
		}
		String psw= MD5Util.GetMD5Code(password);
		UsernamePasswordToken token = new UsernamePasswordToken(username,psw);// 使用UsernamePasswordToken对象封装用户名及密码信息
		try {
			subject.login(token);
			map.clear();
			User user = userService.login(username,psw);
			map.put("user", user);
			session.setAttribute("user", user);
			LoginRecordEntity record = new LoginRecordEntity();// 保存登录记录
			record.setUserId(user.getId());
			AccessSiteUtil accessSiteUtil = new AccessSiteUtil();
			// String ipAddr = accessSiteUtil.getIpAddr(request);
			record.setLoginDate(new Timestamp(new Date().getTime()));
			if (!address.equals("")) {
				record.setLoginSite(address);
			} else {
				record.setLoginSite(accessSiteUtil.getAddresses("ip="+ accessSiteUtil.getIpAddr(request), "utf-8"));
			}
			loginRecordService.loginRecored(record);
			return "index";
		} catch (AuthenticationException e) {
			map.clear();
			map.put("username", username);
			map.put("msg1", "密码错误，请重新输入");
			return "forward:/login.jsp";
		}
	}

	@SuppressWarnings("static-access")
	@RequestMapping("/login1")
	public String login(@Param(value = "usename") String username,
			@Param("password") String password,
			@Param("address") String address, HttpServletRequest request,
			ModelMap map, HttpSession session)
			throws UnsupportedEncodingException {
		User user = userService.login(username, MD5Util.GetMD5Code(password));

		if (user == null) {
			map.clear();
			map.put("username", username);
			map.put("msg1", "密码错误，请重新输入");
			return "forward:/login.jsp";
		} else if (user.getUsername().equals(username)
				&& user.getPassword().equals(MD5Util.GetMD5Code(password))) {
			map.clear();
			map.put("user", user);
			session.setAttribute("user", user);
			LoginRecordEntity record = new LoginRecordEntity();// 保存登录记录
			record.setUserId(user.getId());
			AccessSiteUtil accessSiteUtil = new AccessSiteUtil();
			// String ipAddr = accessSiteUtil.getIpAddr(request);
			record.setLoginDate(new Timestamp(new Date().getTime()));
			if (!address.equals("")) {
				record.setLoginSite(address);
			} else {
				record.setLoginSite(accessSiteUtil.getAddresses("ip="
						+ accessSiteUtil.getV4IP(), "utf-8"));
			}
			loginRecordService.loginRecored(record);
			return "index";
		}
		return "forward:/login.jsp";
	}

	@RequestMapping("/check")
	public void check(String username, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws IOException {
		String user = userService.check(username);
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		if (username.equals("")) {
			response.getWriter().write("请输入用户名");
		} else if (user == null) {
			response.getWriter().write("用户不存在");
		}
	}

	@RequestMapping("/logout")
	public String check(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		session.removeAttribute("user");
		SecurityUtils.getSubject().logout();
		return "redirect:/login.jsp";
	}

	/**
	 * 获取当前用户近30天的登录记录图标数据
	 * 
	 * @param uid
	 *            用户id
	 * @throws ParseException
	 * */
	@RequestMapping("/getloginrecords")
	public void getloginRecoredForMonth(HttpSession session,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap map) throws JsonGenerationException, JsonMappingException,
			IOException, ParseException {
		User user = (User) session.getAttribute("user");
		List<Map<String, String>> maplis = loginRecordService
				.getloginRecoredForMonth(user.getId());
		Map<Object, Object> resultMap = new HashMap();
		Map<Object, Object> m1 = new LinkedHashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String endDate = sdf.format(new Date());// 当前时间
		String dateformat = null;
		Calendar calendar = Calendar.getInstance();
		for (Map m : maplis) {
			Object t = m.get("t");
			Object c = m.get("c");
			resultMap.put(t, c);
		}
		for (int i = 29; i >= 0; i--) {
			calendar.setTime(sdf.parse(endDate));
			calendar.add(calendar.DATE, -i);
			dateformat = sdf.format(calendar.getTime());
			if (resultMap.get(dateformat) != null) {
				m1.put(dateformat, resultMap.get(dateformat));
				continue;
			}
			m1.put(dateformat, 0);
		}

		String json = new ObjectMapper().writeValueAsString(m1);
		response.getWriter().write(json);
	}

}
