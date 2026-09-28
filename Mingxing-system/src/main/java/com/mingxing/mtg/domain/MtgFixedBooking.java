package com.mingxing.mtg.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mingxing.common.core.domain.BaseEntity;

public class MtgFixedBooking extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long fixedId;
    private String bookingTitle;
    private String startTime;
    private String endTime;
    private String recurrenceType;
    private Integer recurrenceDay;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private Long bookerId;
    private String bookerName;
    private Long deptId;
    private String deptName;
    private Integer attendees;
    private String contactPhone;
    private String meetingLink;
    private String meetingPassword;
    private String serviceItems;
    private String status;
    private List<Long> roomIds;
    private List<String> roomNames;

    public Long getFixedId() { return fixedId; }
    public void setFixedId(Long fixedId) { this.fixedId = fixedId; }

    public String getBookingTitle() { return bookingTitle; }
    public void setBookingTitle(String bookingTitle) { this.bookingTitle = bookingTitle; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getRecurrenceType() { return recurrenceType; }
    public void setRecurrenceType(String recurrenceType) { this.recurrenceType = recurrenceType; }

    public Integer getRecurrenceDay() { return recurrenceDay; }
    public void setRecurrenceDay(Integer recurrenceDay) { this.recurrenceDay = recurrenceDay; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Long getBookerId() { return bookerId; }
    public void setBookerId(Long bookerId) { this.bookerId = bookerId; }

    public String getBookerName() { return bookerName; }
    public void setBookerName(String bookerName) { this.bookerName = bookerName; }

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Long> getRoomIds() { return roomIds; }
    public void setRoomIds(List<Long> roomIds) { this.roomIds = roomIds; }

    public List<String> getRoomNames() { return roomNames; }
    public void setRoomNames(List<String> roomNames) { this.roomNames = roomNames; }
}
