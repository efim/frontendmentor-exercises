package industries.sunshine.todolist

import upickle.default.ReadWriter

object StateModel {
  final case class TaskDescription(description: String, isCompleted: Boolean) derives ReadWriter
}
