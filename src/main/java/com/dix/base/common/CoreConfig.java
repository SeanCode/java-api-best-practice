package com.dix.base.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by dd on 9/8/15.
 */
public class CoreConfig {

    private static volatile CoreConfig instance;

    private List<Pattern> guestCanAccessPathPatternList;

    private CoreConfig() {

        guestCanAccessPathPatternList = new ArrayList<>();
        guestCanAccessPathPatternList.add(Pattern.compile("^/error*"));

    }

    public static CoreConfig getInstance() {
        if (instance == null) {
            synchronized (CoreConfig.class) {
                if (instance == null) {
                    instance = new CoreConfig();
                }
            }
        }
        return instance;
    }


    public void addToGuestCanAccessPathPatternList(Pattern pattern)
    {
        if (guestCanAccessPathPatternList.stream()
                .filter(p -> p.pattern().equals(pattern.pattern()))
                .count() == 0)
        {
            guestCanAccessPathPatternList.add(pattern);
        }
    }

    public void addToGuestCanAccessPathPatternList(List<Pattern> patternList)
    {
        patternList.forEach(this::addToGuestCanAccessPathPatternList);
    }

    public void removeFromGuestCanAccessPathList(Pattern pattern)
    {
        guestCanAccessPathPatternList.removeIf(p -> p.pattern().equals(pattern.pattern()));
    }

    public List<Pattern> getGuestCanAccessPathPatternList()
    {
        return guestCanAccessPathPatternList;
    }


}
