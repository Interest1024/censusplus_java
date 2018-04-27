define(["comp/maininput"], function(MainInput) {
    var Main = React.createClass({
    render: function() {
        return (        
            <div id = "maindiv">
                <MainInput />
            </div>
        );
        }
    });

    return Main;
});