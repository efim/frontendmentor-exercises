package industries.sunshine.todolist

import upickle.default.ReadWriter
import java.util.UUID

object StateModel {
  final case class TaskDescription(description: String, isCompleted: Boolean,
  uuid: String = UUID.randomUUID().toString())
      derives ReadWriter

  val default = List(
    TaskDescription("Complete online JavaScript course", true),
    TaskDescription("Jog around in the park 3x", false),
    TaskDescription("10 minutes meditation", false),
    TaskDescription("Read for 1 hour", false),
    TaskDescription("Pick up groceries", false),
    TaskDescription("Complete Todo App on Frontend Mentor", false),
  )
}
