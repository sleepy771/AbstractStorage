package com.gmail.sleepy771.storage;

import java.nio.file.InvalidPathException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathFactory {
    private final static Pattern GLOBAL_PATH_PATTERN = Pattern
	    .compile("@\\w+://(\\w+/)*(\\w+/?)");
    private final static Pattern LOCAL_PATH_PATTERN = Pattern
	    .compile("/(\\w+/)*\\w+/?");
    private final static Pattern NODE_PATTERN = Pattern.compile("\\w+");

    public static Path parsePath(final String path) {
	if (GLOBAL_PATH_PATTERN.matcher(path).matches()) {
	    GlobalPath.Builder gpb = null;
	    Matcher m = NODE_PATTERN.matcher(path);
	    while (m.find()) {
		if (gpb == null) {
		    gpb = new GlobalPath.Builder(m.group());
		} else {
		    gpb.add(m.group());
		}
	    }
	    return gpb.build();
	} else if (LOCAL_PATH_PATTERN.matcher(path).matches()) {
	    LocalPath.Builder lpb = new LocalPath.Builder();
	    ;
	    Matcher m = NODE_PATTERN.matcher(path);
	    while (m.find()) {
		lpb.add(m.group());
	    }
	    return lpb.build();
	} else
	    throw new InvalidPathException(path, "Doesn't matches pattern: "
		    + GLOBAL_PATH_PATTERN.toString() + " nor "
		    + LOCAL_PATH_PATTERN.toString());
    }
}
