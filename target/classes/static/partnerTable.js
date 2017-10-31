class TableData extends React.Component {  
  constructor () {
    super();
    this.state = { data: null };
  }

  componentDidMount () {
    this.data().then(data => {
      const self = this;
      this.setState({data: data});
    });
  }

  columns () {
    return [
        {key: 'name', label: 'Name'},
        {key: 'age', label: 'Age'},
        {key: 'color', label: 'Color', cell: (obj, key) => {
            return <span>{ obj[key] }</span>;
        }}
    ];
  }

  data () {
    return new Promise((resolve, reject) => {
      resolve([
        { name: 'Louise', age: 27, color: 'red' },
        { name: 'Margaret', age: 15, color: 'blue'},
        { name: 'Lisa', age:34, color: 'yellow'}
      ]);
   });
  }

  render () {
    if (this.state.data !== null) {
      return <JsonTable rows={this.state.data} columns={this.columns()} />;
    }
    return <div>Loading...</div>;
  }
}

ReactDOM.render(
  <TableData />,
  document.getElementById('root')
);