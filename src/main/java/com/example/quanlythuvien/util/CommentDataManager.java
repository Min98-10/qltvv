package com.example.quanlythuvien.util;

import com.example.quanlythuvien.model.Comment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDataManager {
    private static final String FILE_PATH = "data/comments.dat";
    private static CommentDataManager instance;

    private CommentDataManager() {}

    public static CommentDataManager getInstance() {
        if (instance == null) {
            instance = new CommentDataManager();
        }
        return instance;
    }

    public void add(Comment comment) {
        List<Comment> all = loadAll();
        all.add(comment);
        save(all);
    }

    public List<Comment> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Comment>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void save(List<Comment> comments) {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(comments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Comment> getByDocument(String documentTitle) {
        return loadAll().stream()
                .filter(c -> c.getDocumentTitle().equalsIgnoreCase(documentTitle))
                .collect(Collectors.toList());
    }

    public double getAverageStars(String documentTitle) {
        List<Comment> comments = getByDocument(documentTitle);
        if (comments.isEmpty()) return 0.0;

        double total = comments.stream().mapToInt(Comment::getStars).sum();
        return Math.round((total / comments.size()) * 10.0) / 10.0;
    }
}
