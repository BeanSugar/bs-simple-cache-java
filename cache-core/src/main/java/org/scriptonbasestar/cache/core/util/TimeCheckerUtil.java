package org.scriptonbasestar.cache.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

/**
 * @athor archmagece
 * @since 2017-01-17 16
 */
@Slf4j
@UtilityClass
public class TimeCheckerUtil {

	public static boolean checkExpired(Date now, int timeoutSecond){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -timeoutSecond);
		if(log.isTraceEnabled()){
			log.trace("checkExpired param - now : {}, timeoutSecond : {}", now, timeoutSecond);
			log.trace("checkExpired 비교 - now : {}, 비교시간 : {}", now, calendar.getTime());
		}
		return now.after(calendar.getTime());
	}

	public static boolean checkExpired(long now, int timeoutSecond){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -timeoutSecond);
		if(log.isTraceEnabled()){
			log.trace("checkExpired param - now : {}, timeoutSecond : {}", now, timeoutSecond);
			log.trace("checkExpired 비교 - now : {}, 비교시간 : {}", now, calendar.getTime());
		}
		return now > calendar.getTime().getTime();
	}

}
