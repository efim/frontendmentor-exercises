package industries.sunshine.todolist

import scala.scalajs.js
import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import industries.sunshine.todolist.StateModel.TaskDescription
import typings.sortablejs.sortablejsRequire
import java.util.Locale.FilteringMode
import typings.sortablejs.mod.Options

object TasksListComponent {
  def render(
      tasks: Signal[List[TaskDescription]],
      setTaskCompletion: String => Boolean => Unit,
      removeTask: String => () => Unit,
      removeAllCompleted: () => Unit,
      moveTask: (Int, Int) => Unit
  ) = {
    val initSortable = sortablejsRequire
    val filterState = Var[Filtering](Filtering.All)
    val filteredTastsList =
      tasks.combineWith(filterState).map { case (tasksList, currentFilter) =>
        tasksList.filter(currentFilter.isVisible(_))
      }

    div(
      div(
        className := "flex flex-col divide-y divide-very-light-grayish-blue",
        className := "dark:divide-dt-very-dark-grayish-blue-2",
        children <-- filteredTastsList.split(_.uuid) {
          case (taskId, initial, taskSignal) => {
            renderSingleTask(
              taskSignal,
              setTaskCompletion(taskId),
              removeTask(taskId)
            )
          }
        },
        onMountUnmountCallback(
          ctx => {
            val library = typings.sortablejs.mod.^.asInstanceOf[js.Dynamic]
            val options = Options()
            // typings.sortablejs.mod.create(ctx.thisNode.ref, options)
            val sortable = library.Sortable.create(ctx.thisNode.ref, options)
            println(s"ttempting to create sortable i guess $sortable")
          },
          el => {
            val library = typings.sortablejs.mod.^.asInstanceOf[js.Dynamic]
            // typings.sortablejs.mod.get(el.ref)
            val sortable = library.Sortable.get(el.ref)
            sortable.destroy()
          }
        ),
        onMountBind(ctx => {
          eventProp(name = "end") --> Observer[dom.Event](event => {
            val dynamix = event.asInstanceOf[js.Dynamic]
            // these indices are potentially from the "filtered" list
            val fromIndex = dynamix.oldIndex.asInstanceOf[Int]
            val newIndex = dynamix.newIndex.asInstanceOf[Int]

            val filteredList = filteredTastsList.observe(ctx.owner).now()
            val fromItem = filteredList(fromIndex)
            val toItem = filteredList(newIndex)

            val fullList = tasks.observe(ctx.owner).now()
            val fullFrom = fullList.indexOf(fromItem)
            val fullTo = fullList.indexOf(toItem)

            moveTask(fullFrom, fullTo)
          })
        })
      ),
      renderListFooter(
        tasks.map(_.count(!_.isCompleted)),
        filterState,
        removeAllCompleted
      ).amend(
        className := "border-t border-very-light-grayish-blue",
        className := "dark:border-dt-very-dark-grayish-blue-2"
      ),
      renderBottomInfo()
    )

  }

  private def renderSingleTask(
      task: Signal[TaskDescription],
      setTaskCompletion: Boolean => Unit,
      deleteTask: () => Unit
  ) = {
    div(
      className := "flex flex-row items-center p-3 px-4 bg-white",
      className := "dark:bg-dt-very-dark-desaturated-blue",
      className := "md:p-4 md:px-5",
      className := "first:rounded-t",
      div(
        className := "relative mr-2 w-5 h-5",
        className := "md:mr-4",
        input(
          typ := "checkbox",
          checked <-- task.map(_.isCompleted),
          className := "absolute appearance-none cursor-pointer",
          className := "w-5 h-5 rounded-full border border-light-grayish-blue",
          className := "dark:border-dt-very-dark-grayish-blue-2",
          className := "checked:bg-gradient-to-br checked:to-primary-check-purple checked:from-primary-check-blue",
          className := "overflow-hidden",
          onInput.mapToChecked --> Observer(setTaskCompletion(_))
        ),
        img(
          className := "absolute top-1/3 left-1/3 w-2 pointer-events-none",
          className <-- task.map(_.isCompleted).map(if (_) "" else "hidden"),
          src := "/images/icon-check.svg",
          alt := "task is done"
        )
      ),
      p(
        className := "text-xs duration-200 md:text-base md:tracking-tighter grow",
        className <-- task
          .combineWith(Theme.isDarkThemeSignal)
          .map((t, isDark) =>
            // TODO join with isDarkTheme signal here
            (isDark) match {
              case true =>
                if (t.isCompleted) "line-through text-dt-very-dark-grayish-blue"
                else "text-dt-light-grayish-blue"
              case false =>
                if (t.isCompleted) "line-through text-light-grayish-blue"
                else "text-very-dark-grayish-blue"
            }
          ),
        child.text <-- task.map(_.description)
      ),
      button(
        className := "md:hidden",
        img(
          src := "/images/icon-cross.svg",
          alt := "delete the task",
          className := "h-3"
        ),
        onClick --> Observer(_ => deleteTask())
      )
    )
  }

  /** counter, controls to clear all, and apply filters on mobile - in two bars,
    * on desktop - as single bar doing with very confusing grid layout and
    * manual corner rounding, so that white backround would be on elements, and
    * not on Gap
    */
  private def renderListFooter(
      uncompletedCount: Signal[Int],
      listFiltering: Var[Filtering],
      removeAllCompleted: () => Unit
  ) = {
    def renderCount() = {
      p(
        className := "p-3 px-5 bg-white rounded-bl text-dark-grayish-blue",
        className := "dark:bg-dt-very-dark-desaturated-blue dark:text-dt-dark-grayish-blue",
        child.text <-- uncompletedCount.map(_.toString()),
        " items left"
      )
    }

    def renderClearCompleted() = {
      button(
        className := "p-3 px-5 bg-white rounded-br text-dark-grayish-blue",
        className := "dark:bg-dt-very-dark-desaturated-blue dark:text-dt-dark-grayish-blue",
        "Clear Completed",
        onClick --> Observer(_ => removeAllCompleted())
      )
    }

    // each filter control is a component that contains an instance of Filtering
    def renderFilters(listFiltering: Var[Filtering]) = {
      def filterControl(ownFilter: Filtering) = {
        println(s"rendering for $ownFilter, active is ${listFiltering.now()}")
        button(
          className := "px-2",
          ownFilter.toString(),
          className <-- listFiltering.signal
            .map(active => active == ownFilter)
            .map(
              // TODO join with isDarkTheme signal here
              if (_) "text-primary-bright-blue" else "text-dark-grayish-blue"
            ),
          onClick --> Observer(_ => {
            listFiltering.set(ownFilter)
            println(s"setting new filter $ownFilter")
          })
        )
      }

      div(
        className := "grid",
        className := "font-bold bg-white",
        className := "dark:bg-dt-very-dark-desaturated-blue",
        div(
          className := "inline-grid place-content-center grid-cols-[repeat(3,_auto)]",
          filterControl(Filtering.All),
          filterControl(Filtering.Active),
          filterControl(Filtering.Completed)
        )
      )

    }

    div(
      className := "grid gap-y-4 grid-cols-[auto_1fr_auto]",
      className := "text-xs md:text-sm",
      renderCount(),
      div(
        className := "bg-white md:hidden",
        className := "dark:bg-dt-very-dark-desaturated-blue"
      ), // empty space for mobile view
      renderClearCompleted(),
      renderFilters(listFiltering).amend(
        // at least grid classes are added via 'amend' close to grid composition
        className := "col-span-full row-start-2", // mobile hardcode into row 2
        className := "md:col-span-1 md:col-start-2 md:row-start-1", // hardcode desktop into middle of row 1
        className := "p-3 rounded md:rounded-none"
      )
    )
  }

  def renderBottomInfo() = p(
    className := "mt-10 text-sm text-center text-very-dark-grayish-blue",
    "Drag and drop to reorder list"
  )

  sealed trait Filtering {
    def isVisible(task: TaskDescription): Boolean
  }
  object Filtering {
    case object All extends Filtering {
      override def isVisible(task: TaskDescription): Boolean = true
    }
    case object Active extends Filtering {
      override def isVisible(task: TaskDescription): Boolean = !task.isCompleted
    }
    case object Completed extends Filtering {
      override def isVisible(task: TaskDescription): Boolean = task.isCompleted
    }
  }
}
