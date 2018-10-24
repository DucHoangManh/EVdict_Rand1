package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.awt.TextField.*;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.net.URL;
import java.io.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.*;

public class Controller {
	Manage dataManager = new Manage();
	String observeWord = "";
	@FXML
	private TextField wordField;
	@FXML
	private ListView<String> wordList;
	@FXML
	private WebView infoField;
	@FXML
	private Button update;
	@FXML
	private Button delete;
	@FXML
	private Button speak;
	@FXML
	private Label deleteLabel;
	@FXML
	private HTMLEditor inputAddInfo;
	@FXML
	private TextField inputAddWord;
	@FXML
	private Button confirmAddWord;
	@FXML
	private TextArea inputPara;
	@FXML
	private TextArea outputPara;
	@FXML
	private Button confirmTranslatePara;
	@FXML
	private Button comfirmEditWord;
	@FXML
	private HTMLEditor inputEdit;
	@FXML
	private TextField inputEditWord;
	@FXML
	private Button BshowWord;


	private Object fxmlLoader;

	public void init() {
	}

	public void getText() {
		/*
		 * wordField.setText(""); ArrayList<String> init = new ArrayList<String>();
		 * dataManager.findWords(wordField.getText(),init); ObservableList<String>
		 * showList = FXCollections.observableList(init); wordList.setItems(showList);
		 */
		wordField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				ArrayList<String> resultSet = new ArrayList<String>();
				wordList.getItems().clear();
				dataManager.findWords(wordField.getText(), resultSet);
				ObservableList<String> showList = FXCollections.observableList(resultSet);
				wordList.setItems(showList);
			}
		});
		wordList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				observeWord = newValue;
				// System.out.println(observeWord);
				if (observeWord != null) {
					// infoField.setText(dataManager.explain(newValue));
					String content = dataManager.explain(newValue);
					final WebEngine webEngine = infoField.getEngine();
					webEngine.loadContent(content);
				}
			}
		});
	}

	public void updateWord() {
		if (observeWord == null || observeWord == "") {
			Alert updateFail = new Alert(Alert.AlertType.WARNING);
			updateFail.setContentText("Không có từ nào được chọn");
			updateFail.setTitle("Cập nhật từ");
			updateFail.setHeaderText(null);
			updateFail.showAndWait();
			return;
		}
		try {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Cập nhật từ");
			alert.setContentText("Bạn có muốn cập nhật từ " + observeWord + " ?");
			alert.setHeaderText(null);
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == ButtonType.OK) {
				Alert success = new Alert(Alert.AlertType.INFORMATION);
				success.setHeaderText(null);
				success.setTitle("Cập nhật từ");
				success.setContentText("Bạn đã cập nhật thành công từ " + observeWord);
				success.showAndWait();
				// dataManager.editWord(observeWord, infoField.getText());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteWord() {
		try {
			if (observeWord == null || observeWord == "") {
				Alert deleteFail = new Alert(Alert.AlertType.WARNING);
				deleteFail.setContentText("Không có từ nào được chọn!");
				deleteFail.setTitle("Xóa từ");
				deleteFail.setHeaderText(null);
				deleteFail.showAndWait();
				return;
			}
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Xóa từ");
			alert.setContentText("Bạn có chắc chắn muốn xóa từ " + observeWord + " ?");
			alert.setHeaderText(null);
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == ButtonType.OK) {
				Alert success = new Alert(Alert.AlertType.INFORMATION);
				success.setHeaderText(null);
				success.setTitle("Xóa");
				success.setContentText("Bạn đã xóa từ " + observeWord + " !");
				success.showAndWait();
				// wordList.getItems().clear();
				dataManager.deleteWord(observeWord);
				wordList.getItems().remove(observeWord);

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	private void pronounceWord() {
		try {
			Speaker speaker=new Speaker();
			speaker.speak(observeWord);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	@FXML
	private void addWord() throws Exception {
		Parent root;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("addWord.fxml"));
			/*
			 * if "fx:controller" is not set in fxml
			 * fxmlLoader.setController(NewWindowController);
			 */
			Scene scene = new Scene(fxmlLoader.load(), 646, 472);
			Stage stage = new Stage();
			stage.setTitle("Thêm từ");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void addWordToDB() {
		try {
			if (inputAddWord.getText() == null || inputAddWord.getText() == "") {
				Alert deleteFail = new Alert(Alert.AlertType.WARNING);
				deleteFail.setContentText("Hãy nhập đầy đủ thông tin!");
				deleteFail.setTitle("Thêm từ");
				deleteFail.setHeaderText(null);
				deleteFail.showAndWait();
				return;
			}
			dataManager.insertWord(inputAddWord.getText(), inputAddInfo.getHtmlText());
			Alert success = new Alert(Alert.AlertType.INFORMATION);
			success.setHeaderText(null);
			success.setTitle("Thêm từ");
			success.setContentText("Bạn đã thêm thành công từ " + inputAddWord.getText() + " vào từ điển!");
			success.showAndWait();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void paraTrans() {
		try {
			if (inputPara.getText() == null || inputPara.getText() == "") {
				Alert deleteFail = new Alert(Alert.AlertType.WARNING);
				deleteFail.setContentText("Hãy nhập đầy đủ thông tin!");
				deleteFail.setTitle("Dịch đoạn văn");
				deleteFail.setHeaderText(null);
				deleteFail.showAndWait();
				return;
			}
			String input = inputPara.getText();
			String output = dataManager.translator(input);
			outputPara.setText(output);

		} catch (Exception e) {
			Alert paraTransFail = new Alert(Alert.AlertType.WARNING);
			paraTransFail.setContentText("Hãy kết nối mạng để thực hiện chức năng này!");
			paraTransFail.setTitle("Thông báo");
			paraTransFail.setHeaderText(null);
			paraTransFail.showAndWait();
			System.out.println(e.getMessage());
		}
	}

	@FXML
	private void translatePara() throws Exception {
		Parent root;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("paraTrans.fxml"));
			/*
			 * if "fx:controller" is not set in fxml
			 * fxmlLoader.setController(NewWindowController);
			 */
			Scene scene = new Scene(fxmlLoader.load(), 600, 400);
			Stage stage = new Stage();
			stage.setTitle("Dịch đoạn văn");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void editWord() {
		Parent root;
		try {
			System.out.println(observeWord);
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("editWord.fxml"));
			/*
			 * if "fx:controller" is not set in fxml
			 * fxmlLoader.setController(NewWindowController);
			 */
			Scene scene = new Scene(fxmlLoader.load(), 628, 607);
			Stage stage = new Stage();
			stage.setTitle("Cập nhật từ");
			stage.setScene(scene);
			stage.show();
			System.out.println(observeWord);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@FXML
	public void showEditWord() { 
		try {
			String word = inputEditWord.getText();
			inputEdit.setHtmlText(dataManager.explain(word));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void editWordDB() {
		try {
			String word = inputEditWord.getText();
			String info = inputEdit.getHtmlText();
			dataManager.editWord(word, info);
			Alert success = new Alert(Alert.AlertType.INFORMATION);
			success.setHeaderText(null);
			success.setTitle("Cập nhật từ");
			success.setContentText("Bạn đã cập nhật từ " + word + " thành công!");
			success.showAndWait();
		} catch (Exception e) {
			Alert updateFail = new Alert(Alert.AlertType.WARNING);
			updateFail.setContentText("Chưa cập nhật được từ!");
			updateFail.setTitle("Cập nhật từ");
			updateFail.setHeaderText(null);
			updateFail.showAndWait();
			System.out.println(e.getMessage());
		}
		
	}
}
