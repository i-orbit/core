package com.inmaytide.orbit.core;

import com.inmaytide.orbit.core.utils.FileUploadUtils;

import java.nio.file.Paths;

/**
 * @author inmaytide
 * @since 2024/4/10
 */
public class FileSHA256Tester {

    public static void main(String[] args) {
        String file1 = "/Users/inmaytide/Downloads/123123123123213213123.log";
        System.out.println(FileUploadUtils.getFileSHA256(Paths.get(file1)));
        String file2 = "/Users/inmaytide/Downloads/metrics.log";
        System.out.println(FileUploadUtils.getFileSHA256(Paths.get(file2)));
    }

}
