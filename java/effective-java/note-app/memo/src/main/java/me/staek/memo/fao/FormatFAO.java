package me.staek.memo.fao;

import me.staek.memo.Format;
import me.staek.memo.common.Util;

import java.awt.*;
import java.io.*;
import java.util.Optional;

/**
 * 서식파일을 접근해서 추가,변경,조회를 진행
 * Fil Access Object
 */
public class FormatFAO implements AutoCloseable {
    private static Optional<Format> cache = Optional.of(new Format());
    private static String prefix = "cfg/";
    private static String postfix = ".cfg";

    public static void clear() {
        cache = null;
    }
    public static Optional<Format> getFormat(String filename) {
        if (cache != null) {
            return cache;
        } else {
            try {
                DataInputStream info = new DataInputStream(new FileInputStream(prefix + filename + postfix));
                Format format = (Format) Util.convertBytesToObject(info.readAllBytes());
                cache = Optional.ofNullable(format);
                return cache;
            } catch (FileNotFoundException e) {
                System.out.println("서식이 없다." + e.getMessage());
                return Optional.empty();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void save(String fileName) {
        if (cache == null) throw new RuntimeException();
        try {
            DataOutputStream config = new DataOutputStream(new FileOutputStream(prefix + fileName + postfix));
            config.write(Util.convertObjectToBytes(cache));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void edit(Font font) {
        cache.get().setFontSize(font.getSize());
        cache.get().setFontName(font.getName());
        cache.get().setFontStyle(font.getStyle());
    }

    public static void createFormat(String s) {
        cache = Optional.of(new Format());
    }

    @Override
    public void close() throws Exception {
        cache = null;
    }
}
