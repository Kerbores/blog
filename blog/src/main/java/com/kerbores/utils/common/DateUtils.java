package com.kerbores.utils.common;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.nutz.lang.Times;

/**
 * @author Ixion 时间处理
 */
public class DateUtils extends Times {
	/**
	 * 日期加减
	 * 
	 * @param base
	 *            开始日期
	 * @param days
	 *            增加天数 正数为之后日期负数为之前日期
	 * @return 目标日期
	 */
	public static Date addDays(Date base, int days) {
		return D(base.getTime() + days * 24 * 60 * 60 * 1000l);
	}

	/**
	 * 从今日开始的日期加减
	 * 
	 * @param 增加天数
	 *            正数为之后日期负数为之前日期
	 * @return 目标日期
	 */
	public static Date addDays(int days) {
		return addDays(now(), days);
	}

	/**
	 * 两个日期之间的天数差
	 * 
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return 天数
	 */
	public static long daysBetween(Date start, Date end) {
		return new BigDecimal((end.getTime() - start.getTime()) / 1000 / 24 / 60 / 60).setScale(0, BigDecimal.ROUND_CEILING).toBigInteger()
				.longValue();
	}

	/**
	 * 获取当前日期的周开始<周一或者周日>
	 * 
	 * @param startFromSunday
	 *            从周日开始计算周 标识
	 * @return startFromSunday 为true 则返回上一个周日 为false则返回上一个周一
	 */
	public static Date getWeekStart(boolean startFromSunday) {
		return addDays(getDayStart(), -1 * (Times.C(now()).get(Calendar.DAY_OF_WEEK) - 1 - (startFromSunday ? 0 : 1)));
	}

	/**
	 * 获取当前日期的周结束<周日或者周六>
	 * 
	 * @param startFromSunday
	 *            从周日开始计算周 标识
	 * @return startFromSunday 为true 则返回下一个周六 为false则返回下一个周日
	 */
	public static Date getWeekEnd(boolean startFromSunday) {
		return addDays(getWeekStart(startFromSunday), 6);
	}

	/**
	 * 获取今日凌晨
	 * 
	 * @return 今日凌晨0:0:0的时间实例
	 */
	public static Date getDayStart() {
		return D(format("yyyy-MM-dd 00:00:00", now()));
	}

}
