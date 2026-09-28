package com.mingxing.mtg.service;

import java.util.Date;
import java.util.List;
import com.mingxing.mtg.domain.MtgBooking;

public interface IMtgBookingService {

    MtgBooking selectBookingById(Long bookingId);

    List<MtgBooking> selectBookingList(MtgBooking booking);

    List<MtgBooking> selectBookingByDateRange(Date startDate, Date endDate);

    boolean checkTimeConflict(MtgBooking booking);

    int insertBooking(MtgBooking booking);

    int insertBookingChecked(MtgBooking booking);

    int updateBooking(MtgBooking booking);

    int cancelBooking(MtgBooking booking);

    int auditBooking(MtgBooking booking);

    int deleteBookingByIds(Long[] bookingIds);

    int updateBookingHidden(Long bookingId, Long hiddenBy);
}
