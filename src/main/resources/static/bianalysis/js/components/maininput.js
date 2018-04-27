define([], function() {
    var MainInputPanel = React.createClass({
        render: function(){
            return (
                <input id="mainInputBox" value={this.state.value} onChange={this.handleChange} />
            );
        },

        handleChange: function(evt) {
            this.setState({
            value: evt.target.value
            });
        },
        getInitialState: function() {
            return {
            value: "Suburb Name"
            };
        },
        
        componentDidMount: function() {
        }
    });

    return MainInputPanel;
});