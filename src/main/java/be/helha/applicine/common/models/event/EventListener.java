package be.helha.applicine.common.models.event;

public interface EventListener {
    //permet de définir ce que l'on veut faire quand on reçoit un event
    void onEventReceived(Event event);
}
