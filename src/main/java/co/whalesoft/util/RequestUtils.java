
package co.whalesoft.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

	/**
	 * ajax 요청여부
	 *
	 * @param request HttpServletRequest
	 * @return ajax 요청 여부
	 */
	public static boolean isAjaxRequest( HttpServletRequest request ) {

		String requestHeader = request.getHeader("x-requested-with");
		if ( "XMLHttpRequest".equals(requestHeader) ) {
			return true;
		}
		return false;
	}
	/**
	 * ContextPath를 가져온다.
	 * @param request
	 * @return
	 */
	public static String getContextPath( HttpServletRequest request ) {
		return request.getContextPath();
	}
	
	/**
	 * 최상위 root 패스를 가져온다.(메인)
	 * @param request
	 * @return
	 */
	public static String getRootPath( HttpServletRequest request ) {
		return request.getContextPath();
	}
	
	/**
	 * ContextPath를 제외한 RequestURI를 구한다.
	 * @param request HttpServletRequest
	 * @return URI
	 */
	public static String getRequestURIExcludeContextPath( HttpServletRequest request ) {

		String uri = request.getRequestURI();
		int pathLength = request.getContextPath() == null ? 0 : request.getContextPath().length();

		return uri.substring( pathLength );
	}
	
	/**
	 * 클라이언트 IP주소를 구한다.
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr( HttpServletRequest request ) {

		String clientIp = request.getHeader( "CLIENT_IP" );
		if ( clientIp != null ) return clientIp;

		return request.getRemoteAddr();
	}
}
