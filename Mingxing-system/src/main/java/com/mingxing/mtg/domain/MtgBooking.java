package com.mingxing.mtg.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mingxing.common.core.domain.BaseEntity;

public class MtgBooking extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long bookingId;
    private Long roomId;
    private String roomName;
    private String roomNameEn;
    private String subject;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;

    private String bookingType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Long bookerId;
    private String bookerName;
    private Long deptId;
    private String deptName;
    private String deptNameEn;
    private Integer attendees;
    private String contactPhone;
    private String meetingLink;
    private String meetingPassword;
    private String serviceItems;
    private Long fixedId;
    private String bookingStatus;
    private Integer isPeriodic;
    private String periodicType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date periodicEndDate;

    private Long parentBookingId;
    private String periodicRule;
    private String cancelReason;
    private String delFlag;
    private Long hiddenBy;

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getRoomNameEn() { return roomNameEn; }
    public void setRoomNameEn(String roomNameEn) { this.roomNameEn = roomNameEn; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public String getBookingType() { return bookingType; }
    public void setBookingType(String bookingType) { this.bookingType = bookingType; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public Long getBookerId() { return bookerId; }
    public void setBookerId(Long bookerId) { this.bookerId = bookerId; }

    public String getBookerName() { return bookerName; }
    public void setBookerName(String bookerName) { this.bookerName = bookerName; }

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getDeptNameEn() { return deptNameEn; }
    public void setDeptNameEn(String deptNameEn) { this.deptNameEn = deptNameEn; }

    public Integer getAttendees() { return attendees; }
    public void setAttendees(Integer attendees) { this.attendees = attendees; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }

    public String getMeetingPassword() { return meetingPassword; }
    public void setMeetingPassword(String meetingPassword) { this.meetingPassword = meetingPassword; }

    public String getServiceItems() { return serviceItems; }
    public void setServiceItems(String serviceItems) { this.serviceItems = serviceItems; }

    public Long getFixedId() { return fixedId; }
    public void setFixedId(Long fixedId) { this.fixedId = fixedId; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public Integer getIsPeriodic() { return isPeriodic; }
    public void setIsPeriodic(Integer isPeriodic) { this.isPeriodic = isPeriodic; }

    public String getPeriodicType() { return periodicType; }
    public void setPeriodicType(String periodicType) { this.periodicType = periodicType; }

    public Date getPeriodicEndDate() { return periodicEndDate; }
    public void setPeriodicEndDate(Date periodicEndDate) { this.periodicEndDate = periodicEndDate; }

    public Long getParentBookingId() { return parentBookingId; }
    public void setParentBookingId(Long parentBookingId) { this.parentBookingId = parentBookingId; }

    public String getPeriodicRule() { return periodicRule; }
    public void setPeriodicRule(String periodicRule) { this.periodicRule = periodicRule; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    public Long getHiddenBy() { return hiddenBy; }
    public void setHiddenBy(Long hiddenBy) { this.hiddenBy = hiddenBy; }
}
