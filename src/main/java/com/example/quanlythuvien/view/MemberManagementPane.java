package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MemberManagementPane extends VBox {

    private final TableView<Member> table;
    private final ObservableList<Member> members;

    public MemberManagementPane() {
        setSpacing(15);
        setPadding(new Insets(20));

        Label title = new Label("👥 Quản lý thành viên");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Tìm kiếm
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Tìm kiếm tên đăng nhập hoặc họ tên");
        searchField.setStyle("-fx-font-size: 14px;");
        searchField.setMaxWidth(Double.MAX_VALUE);

        members = FXCollections.observableArrayList(
                new Member("khanh01", "Nguyễn Khánh", "01/02/2002", "123456", "khanh@gmail.com", "Quận 10"),
                new Member("anhnguyen", "Nguyễn Văn A", "15/04/2001", "987654", "anva@gmail.com", "Quận 3"),
                new Member("linhvu", "Vũ Thị Linh", "20/10/2003", "001122", "linh@gmail.com", "Hà Nội")
        );

        FilteredList<Member> filtered = new FilteredList<>(members, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String keyword = newVal.toLowerCase();
            filtered.setPredicate(m -> m.getUsername().toLowerCase().contains(keyword)
                    || m.getFullName().toLowerCase().contains(keyword));
        });

        table = new TableView<>(filtered);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-font-size: 14px;");

        TableColumn<Member, String> userCol = new TableColumn<>("Tên đăng nhập");
        userCol.setCellValueFactory(data -> data.getValue().usernameProperty());

        TableColumn<Member, String> nameCol = new TableColumn<>("Họ tên");
        nameCol.setCellValueFactory(data -> data.getValue().fullNameProperty());

        TableColumn<Member, String> dobCol = new TableColumn<>("Ngày sinh");
        dobCol.setCellValueFactory(data -> data.getValue().birthDateProperty());

        TableColumn<Member, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<Member, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<Member, String> addressCol = new TableColumn<>("Địa chỉ");
        addressCol.setCellValueFactory(data -> data.getValue().addressProperty());

        TableColumn<Member, Void> actionCol = new TableColumn<>("Hành động");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button delBtn = new Button("🗑");
            private final HBox box = new HBox(5, editBtn, delBtn);

            {
                box.setAlignment(Pos.CENTER);
                editBtn.setOnAction(e -> {
                    Member m = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sửa thành viên");
                    alert.setContentText("Sửa demo: " + m.getUsername());
                    alert.show();
                });
                delBtn.setOnAction(e -> {
                    Member m = getTableView().getItems().get(getIndex());
                    members.remove(m);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(box);
            }
        });

        table.getColumns().addAll(userCol, nameCol, dobCol, idCol, emailCol, addressCol, actionCol);

        Button addBtn = new Button("➕ Thêm thành viên (demo)");
        addBtn.setStyle("-fx-font-size: 14px;");
        addBtn.setOnAction(e -> {
            members.add(new Member("moi123", "Thành viên mới", "01/01/2000", "ABC123", "new@email.com", "Địa chỉ mới"));
        });

        // ===== Layout cuối =====
        getChildren().addAll(title, searchField, table, addBtn);
        VBox.setVgrow(table, Priority.ALWAYS); // 💡 mở rộng table tối đa
    }
}
