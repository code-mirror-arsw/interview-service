package com.code_room.interview_service.domain.model.enums;

public enum NotificationType {
    // Notificación enviada cuando queda una hora para la entrevista
    INTERVIEW_REMINDER_ONE_HOUR,

    // Notificación enviada con el link de la entrevista cuando es aceptado
    INTERVIEW_ACCEPTED_LINK,

    // Notificación de que se programó una nueva entrevista
    INTERVIEW_SCHEDULED,

    // Notificación de rechazo de postulación
    APPLICATION_REJECTED,
}
