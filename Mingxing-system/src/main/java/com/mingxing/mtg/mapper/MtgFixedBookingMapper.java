package com.mingxing.mtg.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.mingxing.mtg.domain.MtgFixedBooking;

public interface MtgFixedBookingMapper {

    MtgFixedBooking selectFixedBookingById(Long fixedId);

    List<MtgFixedBooking> selectFixedBookingList(MtgFixedBooking fixedBooking);

    int insertFixedBooking(MtgFixedBooking fixedBooking);

    int updateFixedBooking(MtgFixedBooking fixedBooking);

    int deleteFixedBookingByIds(Long[] fixedIds);

    void deleteRoomByFixedId(Long fixedId);

    void insertRoomBatch(@Param("fixedId") Long fixedId, @Param("roomIds") List<Long> roomIds);

    List<Long> selectRoomIdsByFixedId(Long fixedId);

    int deleteGeneratedBookingsByFixedId(Long fixedId);
}
