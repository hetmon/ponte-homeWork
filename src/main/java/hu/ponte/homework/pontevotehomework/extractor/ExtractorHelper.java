package hu.ponte.homework.pontevotehomework.extractor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ExtractorHelper {

    public static Long extractUserIdFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (Long) session.getAttribute("userId");
    }
}
