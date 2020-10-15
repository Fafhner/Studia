using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace MvcNews.Models
{
    public class NewsItem
    {
        public int Id { get; set; }
        [DataType(DataType.Date)]
        public DateTime TimeStamp { get; set; }
        [Required]
        [StringLength(140, MinimumLength =5)]
        public string Text { get; set; }
        [Timestamp]
        public byte[] RowVersion { get; set; }

        public NewsItem() {}


    }
}
