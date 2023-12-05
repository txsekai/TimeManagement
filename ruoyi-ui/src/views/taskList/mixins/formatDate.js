const DateMixin = {
  methods: {
    formatDate(datetime) {
      if(datetime == null || datetime == '') {
        return '';
      }

      const temp = new Date(datetime);
      const year = temp.getFullYear();
      const month = temp.getMonth() + 1;
      const day = temp.getDate();

      let formattedDate = '';

      const current = new Date();
      if(year == current.getFullYear() && month == current.getMonth()+1 && day == current.getDate()) {
        formattedDate = '今天';
      }else {
        formattedDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
      }

      let formattedTime = '';

      // TODO 这个判断有问题，如果用户开始时间选了选了23:59
      if((temp.getHours() == 0 && temp.getMinutes() == 0) || (temp.getHours() == 23 && temp.getMinutes() == 59)) {
        formattedTime = '';
      }else {
        const hours = temp.getHours();
        const minutes = temp.getMinutes();

        formattedTime = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`
      }

      return `${formattedDate} ${formattedTime}`;
    }
  }
}

export default DateMixin
