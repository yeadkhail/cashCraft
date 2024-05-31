package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import java.sql.SQLException;

public class FilterController {
    @FXML
    public CheckBox amount_filter;

    @FXML
    public CheckBox people_filter;

    @FXML
    public CheckBox place_filter;

    @FXML
    public CheckBox desc_filter;

    @FXML
    public CheckBox note_filter;

    @FXML
    public CheckBox cat_filter;

    @FXML
    public CheckBox date_filter;

    @FXML
    public CheckBox src_filter;

    @FXML
    public CheckBox dest_filter;

    @FXML
    public CheckBox trans_id;

    private TransactionsController transactionsController;

    public void initData(TransactionsController transactionsController) {
        this.transactionsController = transactionsController;
        loadCheckboxState();

        // Add listeners to checkboxes to update column visibility when changed
        amount_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        people_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        place_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        desc_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        note_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        cat_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        date_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        src_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        dest_filter.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        trans_id.setOnAction(e -> {
            try {
                updateColumnVisibility();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void loadCheckboxState() {
        amount_filter.setSelected(transactionsController.amount_column.isVisible());
        people_filter.setSelected(transactionsController.people_column.isVisible());
        place_filter.setSelected(transactionsController.place_column.isVisible());
        cat_filter.setSelected(transactionsController.cat_column.isVisible());
        desc_filter.setSelected(transactionsController.desc_column.isVisible());
        src_filter.setSelected(transactionsController.src_column.isVisible());
        dest_filter.setSelected(transactionsController.dest_column.isVisible());
        trans_id.setSelected(transactionsController.trans_column.isVisible());
        note_filter.setSelected(transactionsController.note_column.isVisible());
        date_filter.setSelected(transactionsController.date_column.isVisible());
    }

    private void updateColumnVisibility() throws SQLException {
        transactionsController.updateColumnVisibility(this);
    }
}
