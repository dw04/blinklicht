<?php
class Command_Module extends PBEnum
{
  const NONE  = 1;
  const CONSTANT  = 2;
  const CURSOR  = 3;
  const FADE  = 4;
  const RANDOM  = 5;
  const SCREEN  = 6;
}
class Command_Action extends PBEnum
{
  const START  = 1;
  const STOP  = 2;
  const PAUSE  = 3;
  const RESUME  = 4;
}
class Command extends PBMessage
{
  var $wired_type = PBMessage::WIRED_LENGTH_DELIMITED;
  public function __construct($reader=null)
  {
    parent::__construct($reader);
    $this->fields["1"] = "Command_Module";
    $this->values["1"] = "";
    $this->fields["2"] = "Command_Action";
    $this->values["2"] = "";
  }
  function module()
  {
    return $this->_get_value("1");
  }
  function set_module($value)
  {
    return $this->_set_value("1", $value);
  }
  function action()
  {
    return $this->_get_value("2");
  }
  function set_action($value)
  {
    return $this->_set_value("2", $value);
  }
}
?>