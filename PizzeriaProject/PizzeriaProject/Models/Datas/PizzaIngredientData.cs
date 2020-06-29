using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace PizzeriaProject.Models.Datas
{
    public class PizzaIngredientData
    {
        [Key]
        public int Id { set; get; }

        [StringLength(25, MinimumLength=3, ErrorMessage = "Ingredient name must be between 3 and 25 characters long.")]
        [Required]
        [Remote(action:"VerifyNameExist", controller: "Ingredients", AdditionalFields="Id")]
        public string Name { set; get; }

        [Column(TypeName = "money")]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:c}")]
        [DataType(DataType.Currency)]
        public decimal Price { set; get; }

    }
}
