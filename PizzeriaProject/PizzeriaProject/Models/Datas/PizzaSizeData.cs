using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace PizzeriaProject.Models.Datas
{
    public class PizzaSizeData
    {
        [Key]
        public int Id { set; get; }

        [Required(ErrorMessage = "Please add pizza size name.")]
        [StringLength(25, MinimumLength = 3, ErrorMessage = "Size name must be between 3 and 25 characters long.")]
        public String Size { set; get; }


        [Column(TypeName = "money")]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:c}")]
        [DataType(DataType.Currency)]
        public decimal Price { set; get; }
    }
}
