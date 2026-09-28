package com.mingxing.mtg.service;

import java.util.List;
import com.mingxing.mtg.domain.MtgFixedBooking;

public interface IMtgFixedBookingService {

    MtgFixedBooking selectFixedBookingById(Long fixedId);

    List<MtgFixedBooking> selectFixedBookingList(MtgFixedBooking fixedBooking);

    int insertFixedBooking(MtgFixedBooking fixedBooking);

    int updateFixedBooking(MtgFixedBooking fixedBooking);

    int deleteFixedBookingByIds(Long[] fixedIds);
}
