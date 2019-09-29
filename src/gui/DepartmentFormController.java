package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable
{
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	private Department department;
	
	private DepartmentService service;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML 
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction(ActionEvent event)
	{
		if(department == null)
		{
			throw new IllegalStateException("Entity was null");
		}
		
		if(service == null)
		{
			throw new IllegalStateException("Service was null");
		}
		
		try
		{
			department = getFormData();
			service.saveOrUpdate(department);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(DbException e)
		{
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners)
		{
			listener.onDataChanged();
		}
		
	}

	private Department getFormData() 
	{
		Department obj = new Department();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event)
	{
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	public void setDepartmentService (DepartmentService service)
	{
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener)
	{
		dataChangeListeners.add(listener);
	}
	public void setDepartment(Department department)
	{
		this.department = department;
	}
	
	public void updateFormData()
	{
		if(department == null)
		{
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(department.getName());
	}
	private void initializeNodes()
	{
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	

}
