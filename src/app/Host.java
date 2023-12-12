package app;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Host {
    @Getter
    private static String owner;
    @Getter
    private static List<Announcement> announcements;

    public Host() {
    }

    public static void setAnnouncements(List<Announcement> announcements) {
        Host.announcements = announcements;
    }

    /**
     * add announcement
     */
    public static void addAnnouncement(final Announcement announcement) {
        if (announcements == null) {
            announcements = new ArrayList<>();
        }
        announcements.add(announcement);
    }

    /**
     * add announcement
     */
    public static void removeAnnouncement(final Announcement announcement) {
        announcements.remove(announcement);
    }
}
