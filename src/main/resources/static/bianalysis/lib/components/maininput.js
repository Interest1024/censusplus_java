"use strict";

define([], function () {
    var MainInputPanel = React.createClass({
        displayName: "MainInputPanel",

        render: function render() {
            return React.createElement("input", { id: "mainInputBox", value: this.state.value, onChange: this.handleChange });
        },

        handleChange: function handleChange(evt) {
            this.setState({
                value: evt.target.value
            });
        },
        getInitialState: function getInitialState() {
            return {
                value: "Suburb Name"
            };
        },

        componentDidMount: function componentDidMount() {}
    });

    return MainInputPanel;
});