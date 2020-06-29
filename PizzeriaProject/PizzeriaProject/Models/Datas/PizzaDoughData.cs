using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace PizzeriaProject.Models.Datas
{
    public class PizzaDoughData
    { 

        [Key]
        public int Id { set; get; }

        [Required(ErrorMessage = "Please provide pizza dough type")]
        [StringLength(25, MinimumLength = 4, ErrorMessage = "Dough name must be between 4 and 25 characters long.")]
        public String Type { set; get; }

        [Column(TypeName="money")]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:n}")]
        [DataType(DataType.Currency)]
        public decimal Price { set; get; }
    }
}
