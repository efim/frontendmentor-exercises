package industries.sunshine.todolist

import upickle.default.ReadWriter
import java.util.UUID

object StateModel {
  final case class TaskDescription(description: String, isCompleted: Boolean,
  uuid: String = UUID.randomUUID().toString())
      derives ReadWriter
}
