"use strict";

define(["comp/maininput"], function (MainInput) {
    var Main = React.createClass({
        displayName: "Main",

        render: function render() {
            return React.createElement(
                "div",
                { id: "maindiv" },
                React.createElement(MainInput, null)
            );
        }
    });

    return Main;
});