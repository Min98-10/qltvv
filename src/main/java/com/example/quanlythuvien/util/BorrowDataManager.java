package com.example.quanlythuvien.util;

import com.example.quanlythuvien.model.BorrowRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BorrowDataManager {
    private static final String FILE_PATH = "data/borrow.dat";

    public static List<BorrowRecord> load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<BorrowRecord> records = (List<BorrowRecord>) ois.readObject();
            return fixMissingIds(records); // xử lý nếu dữ liệu cũ thiếu ID
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void save(List<BorrowRecord> records) {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void add(BorrowRecord record) {
        if (record.getId() == null || record.getId().isEmpty()) {
            record.setId(UUID.randomUUID().toString());
        }

        List<BorrowRecord> records = load();
        records.add(record);
        save(records);
    }

    public static void updateRecord(BorrowRecord updated) {
        if (updated == null || updated.getId() == null) return;

        List<BorrowRecord> records = load();
        for (int i = 0; i < records.size(); i++) {
            BorrowRecord r = records.get(i);
            if (r.getId() != null && r.getId().equals(updated.getId())) {
                records.set(i, updated);
                break;
            }
        }
        save(records);
    }

    public static List<BorrowRecord> findByUsername(String username) {
        List<BorrowRecord> all = load();
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord r : all) {
            if (r.getUsername().equals(username)) result.add(r);
        }
        return result;
    }

    // === Khắc phục bản ghi thiếu ID ===
    private static List<BorrowRecord> fixMissingIds(List<BorrowRecord> records) {
        boolean changed = false;
        for (BorrowRecord r : records) {
            if (r.getId() == null || r.getId().isEmpty()) {
                r.setId(UUID.randomUUID().toString());
                changed = true;
            }
        }
        if (changed) save(records); // cập nhật lại file nếu có sửa
        return records;
    }
}
