package be.helha.applicine.client.views;

import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Saga;
import be.helha.applicine.common.models.Ticket;
import be.helha.applicine.common.models.Viewable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;

public class TicketPaneViewController {

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView imageMovieView;

    @FXML
    private Label roomLabel;

    @FXML
    private Label ticketVerificationCode;

    @FXML
    private Label ticketTypeLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Text titleMovieLabel;

    @FXML
    private Label versionMovieLabel;

    /**
     * set label with ticket information
     * @param ticket the ticket to display
     */
    public void setTicket(Ticket ticket) {
        dateLabel.setText(ticket.getDate());
        roomLabel.setText("Room " + ticket.getRoom());
        ticketVerificationCode.setText(ticket.getTicketVerificationCode());
        ticketTypeLabel.setText(ticket.getType());
        timeLabel.setText(ticket.getTime());
        System.out.println(ticket.getMovieTitle());
        titleMovieLabel.setText(ticket.getMovieTitle());
        versionMovieLabel.setText(ticket.getMovieVersion());
        setImages(ticket.getMovie().getImage());
    }

    /**
     * set image with movie information
     */

    public void setImages(byte[] image) {
        Image img = new Image(new ByteArrayInputStream(image));
        imageMovieView.setImage(img);
    }

    /**
     * get the fxml resource
     * @return the fxml resource
     */
    public static URL getFXMLResource() {
        return TicketPaneViewController.class.getResource("/be/helha/applicine/client/views/components/ticketPane.fxml");
    }
}

