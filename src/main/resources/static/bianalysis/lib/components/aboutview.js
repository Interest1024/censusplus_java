"use strict";

define([], function () {
    var About = React.createClass({
        displayName: "About",

        render: function render() {
            return React.createElement(
                "div",
                null,
                "This is the about page!"
            );
        }
    });

    return About;
});