package com.mingxing.mtg.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.mingxing.mtg.domain.MtgBooking;

public interface MtgBookingMapper {

    MtgBooking selectBookingById(Long bookingId);

    List<MtgBooking> selectBookingList(MtgBooking booking);

    List<MtgBooking> selectBookingByDateRange(Date startDate, Date endDate);

    int insertBooking(MtgBooking booking);

    int updateBooking(MtgBooking booking);

    int deleteBookingByIds(Long[] bookingIds);

    int checkTimeConflict(MtgBooking booking);

    int updateBookingHidden(@Param("bookingId") Long bookingId, @Param("hiddenBy") Long hiddenBy);
}
