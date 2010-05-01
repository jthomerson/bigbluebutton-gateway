package com.genericconf.bbbgateway.domain;

import junit.framework.TestCase;

public class AttendeeAndMeetingTest extends TestCase {

	public void testHashStuffWorks() throws Exception {
		// had a problem where remove wasn't working.  seemed like a hashcode problem
		//	wrote this test to try to reproduce it
		Meeting mtg = new Meeting();
		Attendee att1 = new Attendee();
		att1.setName("one"); att1.setRole(Role.VIEWER); att1.setUserID("1");
		Attendee att2 = new Attendee();
		att2.setName("two"); att2.setRole(Role.VIEWER); att2.setUserID("2");
		Attendee att3 = new Attendee();
		att3.setName("three"); att3.setRole(Role.VIEWER); att3.setUserID("3");
		
		mtg.attendeeIsJoining(att1);
		mtg.attendeeIsJoining(att2);
		mtg.attendeeIsJoining(att3);
		
		assertEquals(3, mtg.getAttendeesInMeeting());
		mtg.removeAttendee(att1);
		assertEquals(2, mtg.getAttendeesInMeeting());

		Attendee rem = new Attendee();
		rem.setName("two"); rem.setRole(Role.VIEWER); rem.setUserID("2");
		mtg.removeAttendee(rem);
		assertEquals(1, mtg.getAttendeesInMeeting());

	}
}
