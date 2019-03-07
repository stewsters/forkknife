package com.stewsters.forkknife.actions

sealed class ActionResult

object InProgress : ActionResult()

object Succeeded : ActionResult() // its done

object Failed : ActionResult() // hard failed, no suggestions

class Alternative(action: Action) // do this instead
