using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace PizzeriaProject.Migrations
{
    public partial class Update_v03 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_PizzaIngriedients_Pizzas_PizzaId",
                table: "PizzaIngriedients");

            migrationBuilder.AlterColumn<int>(
                name: "PizzaId",
                table: "PizzaIngriedients",
                nullable: true,
                oldClrType: typeof(int),
                oldType: "int");

            migrationBuilder.InsertData(
                table: "PizzaIngriedients",
                columns: new[] { "Id", "IngredientId", "PizzaId", "Quantity" },
                values: new object[] { 4, 6, 1, 1 });


            migrationBuilder.AddForeignKey(
                name: "FK_PizzaIngriedients_Pizzas_PizzaId",
                table: "PizzaIngriedients",
                column: "PizzaId",
                principalTable: "Pizzas",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_PizzaIngriedients_Pizzas_PizzaId",
                table: "PizzaIngriedients");

            migrationBuilder.DeleteData(
                table: "PizzaIngriedients",
                keyColumn: "Id",
                keyValue: 4);

            migrationBuilder.AlterColumn<int>(
                name: "PizzaId",
                table: "PizzaIngriedients",
                type: "int",
                nullable: false,
                oldClrType: typeof(int),
                oldNullable: true);


            migrationBuilder.AddForeignKey(
                name: "FK_PizzaIngriedients_Pizzas_PizzaId",
                table: "PizzaIngriedients",
                column: "PizzaId",
                principalTable: "Pizzas",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
